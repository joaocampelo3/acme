package com.isep.acme.rabbitmqconfigs;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.isep.acme.rabbitmqconfigs.RabbitMQMacros.*;

@Configuration("rabbitMQConfig")
@EnableRabbit
public class RabbitMQConfig {
    private static final Logger LOGGER = Logger.getLogger(RabbitMQConfig.class.getName());

    public ConnectionFactory factory;
    public Connection connectionPublish;
    public Connection connectionReceive;

    public Channel publishChannel;
    public Channel receiveChannel;

    //@PostConstruct
    @Bean
    public void declareQueue() throws Exception {
        // Create a connection factory with RabbitMQ server details
        factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(30000);
        factory.setUsername("guest");
        factory.setPassword("guest");

        // Create a new connection to the server
        connectionPublish = factory.newConnection();
        connectionReceive = factory.newConnection();


        try {
            LOGGER.log(Level.INFO, " Connection: '" + connectionPublish.toString() + "'");
            LOGGER.log(Level.INFO, " Channel Publish: '" + connectionPublish.createChannel().toString() + "'");
        } catch (Exception e) {
            connectionPublish.close();
            throw e;
        }

        try {
            LOGGER.log(Level.INFO, " Connection: '" + connectionReceive.toString() + "'");
            LOGGER.log(Level.INFO, " Channel Receive: '" + connectionReceive.createChannel().toString() + "'");
        } catch (Exception e) {
            connectionReceive.close();
            throw e;
        }

        // Create a new channel on the connection
        publishChannel = connectionPublish.createChannel();
        receiveChannel = connectionReceive.createChannel();

        // Declare the exchange and queues with the specified names
        publishChannel.exchangeDeclare(PUBLISH_EXCHANGE_NAME, "fanout", true);
        receiveChannel.exchangeDeclare(SUBSCRIBE_EXCHANGE_NAME, "fanout", true);

        publishChannel.queueDeclare(PUBLISH_QUEUE_NAME, true, false, false, null);
        LOGGER.log(Level.INFO, " Queue Name: '" + publishChannel.queueDeclare().getQueue() + "'");

        receiveChannel.queueDeclare(SUBSCRIBE_EXCHANGE_NAME, true, false, false, null);
        LOGGER.log(Level.INFO, " Queue Name: '" + receiveChannel.queueDeclare().getQueue() + "'");

        // Bind the publish queue to the exchange with a routing key
        //channel.queueBind(PUBLISH_QUEUE_NAME, EXCHANGE_NAME, VOTES_PUBLISH_KEY);
    }

    /*@Bean
    public Declarables RabbitMQConfigTest() {
        Queue fanoutQueue1 = new Queue(PUBLISH_QUEUE_NAME, false);
        Queue fanoutQueue2 = new Queue(SUBSCRIBE_QUEUE_NAME, false);
        FanoutExchange fanoutExchange = new FanoutExchange(EXCHANGE_NAME);

        return new Declarables(
                fanoutQueue1,
                fanoutQueue2,
                fanoutExchange,
                BindingBuilder.bind(fanoutQueue1).to(fanoutExchange),
                BindingBuilder.bind(fanoutQueue2).to(fanoutExchange));
    }*/

    /*@PreDestroy
    public void closeConnections() throws Exception {
        receiveChannel.close();
        publishChannel.close();
        connectionPublish.close();
        connectionReceive.close();

    }*/

    public Connection getConnectionPublish() {
        return connectionPublish;
    }

    public Connection getConnectionReceive() {
        return connectionReceive;
    }

    public ConnectionFactory getFactory() {
        return factory;
    }
}
