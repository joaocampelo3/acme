package com.isep.acme.LoadBalancer.controller;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;

public class LoadBalancerController {


    @LoadBalanced
    @Bean
    public RestTemplate
}
