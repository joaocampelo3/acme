package com.isep.acme.services.MBCommunication;

import com.isep.acme.events.ProductEvent;
import com.isep.acme.events.ReviewEvent;
import com.isep.acme.model.Product;
import com.isep.acme.rabbitmqconfigs.RabbitMQHost;
import com.isep.acme.repositories.ReviewRepository;
import com.isep.acme.services.interfaces.ReviewService;
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
public class ProductEventSubscriber {

    private static final String EXCHANGE_NAME = "product";

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewService reviewService;

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

        // bind the queue to the exchange for the product events
        channel.queueBind(queueName, EXCHANGE_NAME, "product.*");

        // create a consumer and start consuming messages
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                String eventType = envelope.getRoutingKey().substring(envelope.getRoutingKey().lastIndexOf(".") + 1);
                String originService = envelope.getRoutingKey().substring(0, envelope.getRoutingKey().indexOf("."));
                System.out.println("Received event '" + eventType + "' from service '" + originService + "' with message '" + message + "'");

                // parse the message as a ProductCreatedEvent or ReviewCreatedEvent
                if (eventType.equals("product_created") || eventType.equals("product_updated") || eventType.equals("product_deleted")) {
                    ProductEvent event = ProductEvent.fromJson(message);
                    try {
                        handleProductEvent(eventType, originService, event);
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

    private void handleProductEvent(String eventType, String originService, ProductEvent event) throws Exception {
        // handle the product event
        if (eventType.equals("product_created")) {
            // do something with the product created event
            final Product p = new Product(event.getSku());
            reviewService.createProduct(p);
        } else if (eventType.equals("product_updated")) {
            // do something with the product updated event
        } else if (eventType.equals("product_deleted")) {
            // do something with the product deleted event
            reviewService.DeleteProduct(event.getSku());
        }
    }

    @Bean
    @Async
    public void mainProductSubscription() throws IOException, TimeoutException {
        this.start();
    }

}
