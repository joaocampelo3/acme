package com.isep.acme.services.MBCommunication;

import com.isep.acme.events.VoteEvent;
import com.isep.acme.rabbitmqconfigs.RabbitMQHost;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Publisher {
    private final static String EXCHANGE_NAME = "vote";
    private ConnectionFactory factory;

    @Autowired
    private RabbitMQHost rabbitMQHost;

    public void mainPublish(VoteEvent voteEvent, String routingKey) throws Exception {
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
        String message = voteEvent.toJson();
        channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
        System.out.println("Sent event '" + routingKey + "' with message '" + message + "'");

        // close the channel and connection
        channel.close();
        connection.close();
    }
}