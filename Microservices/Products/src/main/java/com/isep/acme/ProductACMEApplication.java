package com.isep.acme;

import com.isep.acme.model.Product;
import com.isep.acme.property.FileStorageProperties;
import com.isep.acme.rabbitmqconfigs.RabbitMQHost;
import com.isep.acme.repositories.ProductRepository;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
@EnableEurekaClient
public class ProductACMEApplication {

    private static final String RPC_QUEUE_NAME = "q.products_rpc_queue";

    @Autowired
    private static RabbitMQHost rabbitMQHost;
    @Autowired
    private static ProductRepository productRepository;

    public ProductACMEApplication(RabbitMQHost rabbitMQHost, ProductRepository productRepository) {
        ProductACMEApplication.rabbitMQHost = rabbitMQHost;
        ProductACMEApplication.productRepository = productRepository;
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ProductACMEApplication.class, args);

        // Create 1 clients
        RPCClient rpcClient1 = new RPCClient("Product RPC Client");
        rpcClient1.start();

        // Finalize
        rpcClient1.join();
    }

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

    public static class RPCClientImpl implements AutoCloseable {

        private Connection connection;
        private Channel channel;
        private String exclusiveQueueName;

        public RPCClientImpl() throws IOException, TimeoutException {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(rabbitMQHost.getHost());
            factory.setPort(Integer.parseInt(rabbitMQHost.getPort()));
            factory.setUsername(rabbitMQHost.getUsername());
            factory.setPassword(rabbitMQHost.getPassword());

            connection = factory.newConnection();
            channel = connection.createChannel();

            // Tip: use "" to generate random name and don't use auto-delete feature, because "basicCancel"
            // we use in the call() method will delete our queue
            exclusiveQueueName = channel.queueDeclare("", false, true, false, null).getQueue();//.getQueue();
            System.out.println("Queue name:" + exclusiveQueueName);
        }

        private static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
            ByteArrayInputStream in = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(in);
            return is.readObject();
        }

        public static List<?> convertObjectToList(Object obj) {
            if(obj == null)
                return null;

            List<?> list = new ArrayList<>();
            if (obj.getClass().isArray()) {
                list = Arrays.asList((Object[]) obj);
            } else if (obj instanceof Collection) {
                list = new ArrayList<>((Collection<?>) obj);
            }
            return list;
        }

        public List<Product> call() throws IOException, InterruptedException, ExecutionException {
            String corrId = UUID.randomUUID().toString();

            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(corrId)
                    .replyTo(exclusiveQueueName)
                    .build();

            channel.basicPublish(/*exchange*/"", RPC_QUEUE_NAME, props, "GetAllProducts".getBytes(StandardCharsets.UTF_8));

            // Code to consume only one message and stop consuming more messages
            final CompletableFuture<Object> response = new CompletableFuture<>();

            String ctag = channel.basicConsume(exclusiveQueueName, true, (consumerTag, delivery) -> {
                // System.out.println("Got corelation id " + delivery.getProperties().getCorrelationId() + ", expected: " + corrId);
                if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                    try {
                        response.complete(deserialize(delivery.getBody()));
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, consumerTag -> {
            });

            Object result = response.get();
            List<ProductEvent> productEventList = (List<ProductEvent>) convertObjectToList(result);
            List<Product> productList = new ArrayList<>();

            channel.basicCancel(ctag);

            productRepository.deleteAll();

            if (productEventList != null && !productEventList.isEmpty()) {
                Optional<Product> productAux;
                for (ProductEvent productEvent : productEventList) {
                    productAux = productRepository.findBySku(productEvent.getSku());
                    if (productAux.isPresent()){
                        productRepository.updateBySku(productEvent.getSku());
                    } else {
                        productRepository.save(productEvent.toProduct());
                    }
                    productList.add(productEvent.toProduct());
                }
            }

            // return results from RPC service
            return productList;
        }

        public void close() throws IOException {
            connection.close();
        }
    }

    public static class RPCClient extends Thread {
        private final String m_name;
        private RPCClientImpl m_clientImpl;

        public RPCClient(String name) {
            m_name = name;
        }

        public int getRandomNumber(int min, int max) {
            return (int) ((Math.random() * (max - min)) + min);
        }

        public void run() {
            try {
                // Simple delay
                Thread.sleep(5000);

                m_clientImpl = new RPCClientImpl();

                System.out.println(" [x] " + m_name + " requesting GetAllProducts()");
                List<Product> response = m_clientImpl.call();

                if (response != null) {
                    System.out.println(" [.] " + m_name + " got '" + response.toString() + "'");
                }

                Thread.sleep(getRandomNumber(0, 10) * 1000);

                System.out.println(" [x] " + m_name + " request ending...");

            } catch (IOException | InterruptedException | TimeoutException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
