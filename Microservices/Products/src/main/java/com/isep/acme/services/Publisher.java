package com.isep.acme.services;

import com.isep.acme.events.ProductEvent;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Publisher {
    private final static String EXCHANGE_NAME = "product_events";

    public static void main(ProductEvent productEvent, String routingKey) throws Exception {
        // create a connection to the RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        // declare the exchange for the product events
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        // publish the event
        String message = productEvent.toJson();
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
        System.out.println("Sent event '" + routingKey + "' with message '" + message + "'");

        // close the channel and connection
        channel.close();
        connection.close();
    }
}