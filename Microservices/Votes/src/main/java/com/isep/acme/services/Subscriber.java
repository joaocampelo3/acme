package com.isep.acme.services;

import com.rabbitmq.client.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static com.isep.acme.rabbitMQConfigs.RabbitMQMacros.EXCHANGE_NAME;
import static com.isep.acme.rabbitMQConfigs.RabbitMQMacros.SUBSCRIBE_QUEUE_NAME;

public class Subscriber {

    /*public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        channel.queueDeclare(SUBSCRIBE_QUEUE_NAME, false, false, false, null);
        channel.queueBind(SUBSCRIBE_QUEUE_NAME, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(SUBSCRIBE_QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }*/

    @RabbitListener(queues = {SUBSCRIBE_QUEUE_NAME})
    public void receiveMessageFromFanout1(String message) {
        System.out.println("Received fanout 1 message: " + message);
    }
}
