package com.isep.acme;

import com.isep.acme.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import java.awt.image.BufferedImage;

@SpringBootApplication
@EnableConfigurationProperties({FileStorageProperties.class})
public class VoteACMEApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoteACMEApplication.class, args);
    }

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
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
}
