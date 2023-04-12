package com.isep.acme.ProductsBootstrap.services;

import com.isep.acme.ProductsBootstrap.rabbitmqconfigs.RabbitMQHost;
import com.isep.acme.ProductsBootstrap.services.impl.ProductServiceImpl;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

@Service
public class RPCService extends Thread {
    private static final String RPC_QUEUE_NAME = "q.rpc_queue";

    @Autowired
    private RabbitMQHost rabbitMQHost;

    @Autowired
    private ProductServiceImpl productService;

    private List<String> getAllProducts() {
        return productService.getAllProducts();
    }

    public void run() {
        // create a connection to the RabbitMQ server
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(rabbitMQHost.getHost());
        factory.setPort(Integer.parseInt(rabbitMQHost.getPort()));
        factory.setUsername(rabbitMQHost.getUsername());
        factory.setPassword(rabbitMQHost.getPassword());

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel())
        {
            channel.queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
            channel.queuePurge(RPC_QUEUE_NAME);

            channel.basicQos(1);

            System.out.println(" [x] Products Bootstrap Server awaiting RPC requests");

            Object monitor = new Object();

            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(delivery.getProperties().getCorrelationId())
                        .build();

                try {
                    String message = new String(delivery.getBody(), "UTF-8");
                    System.out.println(" [.] Products Bootstrap RPC Service got: " + message + ", replying to:" + delivery.getProperties().getReplyTo() + " with correlation ID: " + delivery.getEnvelope().getDeliveryTag());

                    List<String> response = null;

                    if (message.compareTo("GetAllProducts")==0){
                        response = getAllProducts();
                    }

                    // Replying to the client
                    channel.basicPublish(/*exchange*/"", delivery.getProperties().getReplyTo(), replyProps, serialize(response));
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), /*multiple*/false);

                    // Allow to process next message
                    synchronized (monitor) {
                        monitor.notify();
                    }

                } catch (RuntimeException e) {
                    System.out.println(" [E] " + e.toString());
                }
            };

            // Wait and be prepared to consume the next message
            while (true) {
                channel.basicConsume(RPC_QUEUE_NAME, false, deliverCallback, (consumerTag -> { }));
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

    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
}