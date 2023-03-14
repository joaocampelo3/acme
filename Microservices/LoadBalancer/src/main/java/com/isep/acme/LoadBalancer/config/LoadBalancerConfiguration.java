package com.isep.acme.LoadBalancer.config;

import org.springframework.cloud.client.loadbalancer.LoadBalancerClientsProperties;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadBalancerConfiguration {
    @Bean
    public LoadBalancerClient loadBalancerClient() {
        //return new LoadBalancerClientFactory().getLoadBalancerClient("default");
    }

}
