package com.isep.acme;

import com.isep.acme.property.FileStorageProperties;
import com.isep.acme.rabbitmqconfigs.RabbitMQHost;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;

import java.awt.image.BufferedImage;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageProperties.class
})
@EnableEurekaClient
public class VoteACMEApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoteACMEApplication.class, args);
    }

    @Bean
    public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
        return new BufferedImageHttpMessageConverter();
    }

}
