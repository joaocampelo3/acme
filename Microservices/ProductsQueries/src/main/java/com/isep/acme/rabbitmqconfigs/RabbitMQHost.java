package com.isep.acme.rabbitmqconfigs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;

@Component
@ConfigurationProperties(prefix = "spring.rabbitmq")
@RequiredArgsConstructor
public class RabbitMQHost {

    @Value("${spring.rabbitmq.host}")
    @NotBlank
    private String host;

    @Value("${spring.rabbitmq.port}")
    @NotBlank
    private String port;

    @Value("${spring.rabbitmq.username}")
    @NotBlank
    private String username;

    @Value("${spring.rabbitmq.password}")
    @NotBlank
    private String password;

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
