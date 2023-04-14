package com.isep.acme.services.MBCommunication;

import com.isep.acme.events.ProductEvent;
import com.isep.acme.events.ReviewEvent;
import com.isep.acme.model.DTO.CreateReviewDTO;
import com.isep.acme.rabbitmqconfigs.RabbitMQHost;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class Publisher {
    private final static String EXCHANGE_NAME = "review";
    private ConnectionFactory factory;

    @Autowired
    private RabbitMQHost rabbitMQHost;

    public void mainPublish(ReviewEvent reviewEvent, String routingKey) throws Exception {
        factory = new ConnectionFactory();
        factory.setHost(rabbitMQHost.getHost());
        factory.setPort(Integer.parseInt(rabbitMQHost.getPort()));
        factory.setUsername(rabbitMQHost.getUsername());
        factory.setPassword(rabbitMQHost.getPassword());

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // declare the exchange for the product events
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        // publish the event
        String message = reviewEvent.toJson();
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
        System.out.println("Sent event '" + routingKey + "' with message '" + message + "'");

        // close the channel and connection
        channel.close();
        connection.close();
    }
}