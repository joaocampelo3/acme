package com.isep.acme;

import com.isep.acme.model.ReviewEvent;
import com.isep.acme.rabbitmqconfigs.RabbitMQHost;
import com.isep.acme.repository.ReviewEventRepo;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@EnableConfigurationProperties({RabbitMQHost.class})
@SpringBootApplication
@EnableMongoRepositories("com.isep.acme")
public class ReviewsBootstrapApplication {
    private static final String RPC_QUEUE_NAME = "q.reviews_rpc_queue";
    private static final Logger logger = LoggerFactory.getLogger(ReviewsBootstrapApplication.class);

    @Autowired
    private static RabbitMQHost rabbitMQHost;
    @Autowired
    private static ReviewEventRepo reviewEventRepo;
    private static ConnectionFactory factory;

    public ReviewsBootstrapApplication(RabbitMQHost rabbitMQHost, ReviewEventRepo reviewEventRepo) {
        ReviewsBootstrapApplication.rabbitMQHost = rabbitMQHost;
        ReviewsBootstrapApplication.reviewEventRepo = reviewEventRepo;
    }

    public static void main(String[] args) {
        SpringApplication.run(ReviewsBootstrapApplication.class, args);

        logger.info("RPC executing...");
        // create a connection to the RabbitMQ server
        factory = new ConnectionFactory();
        factory.setHost(rabbitMQHost.getHost());
        factory.setPort(Integer.parseInt(rabbitMQHost.getPort()));
        factory.setUsername(rabbitMQHost.getUsername());
        factory.setPassword(rabbitMQHost.getPassword());

        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            channel.queuePurge(RPC_QUEUE_NAME);

            channel.basicQos(1);

            logger.info(" [x] Reviews Bootstrap Server awaiting RPC requests");

            Object monitor = new Object();

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties.Builder().correlationId(delivery.getProperties().getCorrelationId()).build();

                try {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    logger.info(" [.] Reviews Bootstrap RPC Service got: " + message + ", replying to:" + delivery.getProperties().getReplyTo() + " with correlation ID: " + delivery.getEnvelope().getDeliveryTag());

                    List<String> response = new ArrayList<>();

                    if (message.compareTo("GetAllReviews") == 0) {
                        List<ReviewEvent> reviewEvents = reviewEventRepo.findAll();
                        for (ReviewEvent re: reviewEvents) {
                            response.add(re.toJson());
                        }
                    }

                    String joinedString = String.join(";", response);
                    byte[] messageToSend = joinedString.getBytes(StandardCharsets.UTF_8);

                    // Replying to the client
                    channel.basicPublish(/*exchange*/"", delivery.getProperties().getReplyTo(), replyProps, messageToSend);
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), /*multiple*/false);

                    // Allow to process next message
                    synchronized (monitor) {
                        monitor.notify();
                    }

                } catch (RuntimeException e) {
                    logger.error(" [E] " + e);
                }
            };

            // Wait and be prepared to consume the next message
            while (true) {
                channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> {
                }));
                synchronized (monitor) {
                    try {
                        // don't consume next message as long as current message is processed
                        monitor.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
}