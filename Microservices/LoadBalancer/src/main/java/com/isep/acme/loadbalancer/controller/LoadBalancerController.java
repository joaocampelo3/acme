package com.isep.acme.loadbalancer.controller;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.client.RestTemplate;

public class LoadBalancerController {


    @LoadBalanced
    //@Bean
    public RestTemplate restTemplate;
}
