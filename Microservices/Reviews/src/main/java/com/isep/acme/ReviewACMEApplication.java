package com.isep.acme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

import com.isep.acme.property.FileStorageProperties;

import java.awt.image.BufferedImage;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
@EnableEurekaClient
public class ReviewACMEApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReviewACMEApplication.class, args);
	}

	@Bean
	public HttpMessageConverter<BufferedImage> createImageHttpMessageConverter() {
		return new BufferedImageHttpMessageConverter();
	}
}
