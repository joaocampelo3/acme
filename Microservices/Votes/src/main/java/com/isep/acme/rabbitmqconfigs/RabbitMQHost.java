package com.isep.acme.rabbitmqconfigs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

//@ConfigurationProperties(prefix = "spring.rabbitmq")
@Configuration("rabbitMQProperties")
@Component
public class RabbitMQHost {
    @Value("${spring.rabbitmq.host}")
    public String host;

    @Value("${spring.rabbitmq.port}")
    public String port;

    @Value("${spring.rabbitmq.username}")
    public String username;

    @Value("${spring.rabbitmq.password}")
    public String password;

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return Integer.parseInt(port);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
