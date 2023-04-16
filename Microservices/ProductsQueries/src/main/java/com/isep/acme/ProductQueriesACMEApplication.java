package com.isep.acme;

import com.isep.acme.events.EventTypeEnum;
import com.isep.acme.events.ProductEvent;
import com.isep.acme.model.Product;
import com.isep.acme.property.FileStorageProperties;
import com.isep.acme.rabbitmqconfigs.RabbitMQHost;
import com.isep.acme.repositories.ProductRepository;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
@EnableEurekaClient
public class ProductQueriesACMEApplication {

    private static final String RPC_QUEUE_NAME = "q.products_rpc_queue";
    private static final Logger logger = LoggerFactory.getLogger(ProductQueriesACMEApplication.class);

    @Autowired
    private static RabbitMQHost rabbitMQHost;
    @Autowired
    private static ProductRepository productRepository;

    public ProductQueriesACMEApplication(RabbitMQHost rabbitMQHost, ProductRepository productRepository) {
        ProductQueriesACMEApplication.rabbitMQHost = rabbitMQHost;
        ProductQueriesACMEApplication.productRepository = productRepository;
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ProductQueriesACMEApplication.class, args);

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

        public void call() throws IOException, InterruptedException, ExecutionException {
            String corrId = UUID.randomUUID().toString();

            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(corrId)
                    .replyTo(exclusiveQueueName)
                    .build();

            channel.basicPublish(/*exchange*/"", RPC_QUEUE_NAME, props, "GetAllProducts".getBytes(StandardCharsets.UTF_8));

            // Code to consume only one message and stop consuming more messages
            AtomicReference<List<String>> response = new AtomicReference<>();

            String ctag = channel.basicConsume(exclusiveQueueName, true, (consumerTag, delivery) -> {
                if (delivery.getProperties().getCorrelationId().equals(corrId)) {
                        String decodedString = new String(delivery.getBody(), StandardCharsets.UTF_8);
                        response.set(Arrays.asList(decodedString.split(";")));
                }
            }, consumerTag -> {
            });

            //Simple delay
            Thread.sleep(5000);

            List<ProductEvent> productEventList = new ArrayList<>();
            List<String> productEventStringList = new ArrayList<>();
            productEventStringList = response.get();
            channel.basicCancel(ctag);
            productEventStringList = response.get();

            if (productEventStringList != null && !productEventStringList.isEmpty()) {
                for (String s : productEventStringList) {
                    productEventList.add(ProductEvent.fromJson(s));
                }
            }

            if (productEventList != null && !productEventList.isEmpty()) {
                for (ProductEvent productEvent : productEventList) {
                    if (EventTypeEnum.CREATE.compareTo(productEvent.getEventTypeEnum())==0){
                        logger.info("CREATE ACTION: "+ productEvent.getSku());
                        productRepository.save(productEvent.toProduct());
                    } else if (EventTypeEnum.UPDATE.compareTo(productEvent.getEventTypeEnum())==0) {
                        logger.info("UPDATE ACTION: "+ productEvent.getSku());
                        productRepository.updateBySku(productEvent.toProduct().getSku());
                    } else if (EventTypeEnum.DELETE.compareTo(productEvent.getEventTypeEnum())==0) {
                        logger.info("DELETE ACTION: "+ productEvent.getSku());
                        productRepository.deleteBySku(productEvent.getSku());
                    }
                }
            }
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

                m_clientImpl.call();

                Thread.sleep(getRandomNumber(0, 10) * 1000);

                System.out.println(" [x] " + m_name + " request ending...");

            } catch (IOException | InterruptedException | TimeoutException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
