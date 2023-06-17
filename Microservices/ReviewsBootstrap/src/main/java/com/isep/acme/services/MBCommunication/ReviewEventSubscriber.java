package com.isep.acme.services.MBCommunication;

import com.isep.acme.model.EventTypeEnum;
import com.isep.acme.model.ReviewEvent;
import com.isep.acme.rabbitmqconfigs.RabbitMQHost;
import com.isep.acme.repository.ReviewEventRepo;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Component
@EnableAsync
public class ReviewEventSubscriber {

    private static final String EXCHANGE_NAME = "review";

    @Autowired
    private ReviewEventRepo reviewEventRepo;

    @Autowired
    private RabbitMQHost rabbitMQHost;

    public void start() throws IOException, TimeoutException {

        // create a connection to the RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQHost.getHost());
        factory.setPort(Integer.parseInt(rabbitMQHost.getPort()));
        factory.setUsername(rabbitMQHost.getUsername());
        factory.setPassword(rabbitMQHost.getPassword());

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // create the exchange and queue
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();

        // bind the queue to the exchange for the review events
        channel.queueBind(queueName, EXCHANGE_NAME, "review.*");

        // create a consumer and start consuming messages
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                String eventType = envelope.getRoutingKey().substring(envelope.getRoutingKey().lastIndexOf(".") + 1);
                String originService = envelope.getRoutingKey().substring(0, envelope.getRoutingKey().indexOf("."));
                System.out.println("Received event '" + eventType + "' from service '" + originService + "' with message '" + message + "'");

                // parse the message as a ProductCreatedEvent or ReviewCreatedEvent
                if (eventType.equals("review_created") || eventType.equals("reviewFromVote_created") || eventType.equals("review_updated") || eventType.equals("review_deleted")) {
                    ReviewEvent event = ReviewEvent.fromJson(message);
                    try {
                        handleReviewEvent(eventType, originService, event);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };

        channel.basicConsume(queueName, true, consumer);

        // keep the subscriber running
        System.out.println("Waiting for events...");
        while (true) {
        }
    }

    private void handleReviewEvent(String eventType, String originService, ReviewEvent event) throws Exception {
        // handle the review event
        if (eventType.equals("review_created")) {
            reviewEventRepo.save(new ReviewEvent(event.getIdReview(), event.getReviewUuid(), event.getVersion(), event.getApprovalStatus(), event.getReviewText(), event.getReport(),
                    event.getPublishingDate(), event.getFunFact(), event.getSku(), event.getUserId(), event.getRating(), event.getUpVotes(), event.getDownVotes(), EventTypeEnum.CREATE));
        } else if (eventType.equals("reviewFromVote_created")) {
            reviewEventRepo.save(new ReviewEvent(event.getIdReview(), event.getReviewUuid(), event.getVersion(), event.getApprovalStatus(), event.getReviewText(), event.getReport(),
                    event.getPublishingDate(), event.getFunFact(), event.getSku(), event.getUserId(), event.getRating(), event.getUpVotes(), event.getDownVotes(),  EventTypeEnum.CREATE));
        } else if (eventType.equals("review_updated")) {
            reviewEventRepo.save(new ReviewEvent(event.getIdReview(), event.getReviewUuid(), event.getVersion(), event.getApprovalStatus(), event.getReviewText(), event.getReport(),
                    event.getPublishingDate(), event.getFunFact(), event.getSku(), event.getUserId(), event.getRating(), event.getUpVotes(), event.getDownVotes(),  EventTypeEnum.UPDATE));
        } else if (eventType.equals("review_deleted")) {
            reviewEventRepo.save(new ReviewEvent(event.getIdReview(), event.getReviewUuid(), event.getVersion(), event.getApprovalStatus(), event.getReviewText(), event.getReport(),
                    event.getPublishingDate(), event.getFunFact(), event.getSku(), event.getUserId(), event.getRating(), event.getUpVotes(), event.getDownVotes(),  EventTypeEnum.DELETE));
        }
    }

    @Bean
    @Async
    public void mainReviewSubscription() throws IOException, TimeoutException {
        this.start();
    }

}
