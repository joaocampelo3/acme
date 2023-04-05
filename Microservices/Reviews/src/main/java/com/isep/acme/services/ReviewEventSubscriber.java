package com.isep.acme.services;

import com.isep.acme.events.ProductEvent;
import com.isep.acme.events.ReviewEvent;
import com.isep.acme.model.DTO.CreateReviewDTO;
import com.isep.acme.model.DTO.ReviewDTO;
import com.isep.acme.model.Product;
import com.isep.acme.model.Review;
import com.isep.acme.repositories.ProductRepository;
import com.isep.acme.services.impl.ReviewServiceImpl;
import com.isep.acme.services.interfaces.ReviewService;
import com.rabbitmq.client.*;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.implementation.bind.annotation.Super;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Component
public class ReviewEventSubscriber {

    private static final String EXCHANGE_NAME = "product_events";

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    public ReviewEventSubscriber(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    public void start() throws IOException, TimeoutException {

        // create a connection to the RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // create the exchange and queue
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueName = channel.queueDeclare().getQueue();

        // bind the queue to the exchange for the product events
        channel.queueBind(queueName, EXCHANGE_NAME, "product.*");

        // bind the queue to the exchange for the review events
        channel.queueBind(queueName, EXCHANGE_NAME, "review.*");

        // create a consumer and start consuming messages
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
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
                } else if (eventType.equals("review_created") || eventType.equals("review_updated") || eventType.equals("review_deleted")) {
                    ReviewEvent event = ReviewEvent.fromJson(message);
                    handleReviewEvent(eventType, originService, event);
                }
            }
        };

        channel.basicConsume(queueName, true, consumer);

        // keep the subscriber running
        System.out.println("Waiting for events...");
        while (true) {}
    }

    private void handleProductEvent(String eventType, String originService, ProductEvent event) throws Exception {
        // handle the product event
        if (eventType.equals("product_created")) {
            // do something with the product created event
            final Product p = new Product(event.getSku());
            productRepository.save(p);
           // productRepository.save(p);
        } else if (eventType.equals("product_updated")) {
            // do something with the product updated event
        } else if (eventType.equals("product_deleted")) {
            // do something with the product deleted event
        }
    }

    private void handleReviewEvent(String eventType, String originService, ReviewEvent event) {
        // handle the review event
        if (eventType.equals("review_created")) {
            // do something with the review created event
        } else if (eventType.equals("review_updated")) {
            // do something with the review updated event
        } else if (eventType.equals("review_deleted")) {
            // do something with the review deleted event
        }
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        ReviewEventSubscriber subscriber = new ReviewEventSubscriber(new ProductRepository() {
            @Override
            public Optional<Product> getProductBySku(String sku) {
                return Optional.empty();
            }

            @Override
            public void deleteBySku(String sku) {

            }

            public <S extends Product> S save(S entity) {
                return entity;
            }

            @Override
            public <S extends Product> Iterable<S> saveAll(Iterable<S> entities) {
                return null;
            }

            @Override
            public Optional<Product> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public Iterable<Product> findAll() {
                return null;
            }

            @Override
            public Iterable<Product> findAllById(Iterable<Long> longs) {
                return null;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(Product entity) {

            }

            @Override
            public void deleteAllById(Iterable<? extends Long> longs) {

            }

            @Override
            public void deleteAll(Iterable<? extends Product> entities) {

            }

            @Override
            public void deleteAll() {

            }
        });
        subscriber.start();
    }

}
