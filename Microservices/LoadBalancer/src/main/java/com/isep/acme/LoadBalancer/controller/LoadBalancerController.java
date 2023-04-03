package com.isep.acme.LoadBalancer.controller;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

public class LoadBalancerController {


    @LoadBalanced
    //@Bean
    public RestTemplate restTemplate;
}
