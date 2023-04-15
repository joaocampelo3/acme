package com.isep.acme.loadbalancer.controller;

import com.isep.acme.loadbalancer.model.DTO.CreateReviewDTO;
import com.isep.acme.loadbalancer.model.DTO.ProductDTO;
import com.isep.acme.loadbalancer.model.DTO.ReviewDTO;
import com.isep.acme.loadbalancer.model.Product;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class ProductsBalancerController {

    @Autowired
    private  RestTemplate restTemplate;

    @GetMapping
    public ResponseEntity<Iterable<ProductDTO>> getCatalog() {
        return restTemplate.getForObject("http://PRODUCTACMEAPPLICATION/products", ResponseEntity.class);
    }

    @GetMapping(value = "/products/{sku}")
    public ResponseEntity<ProductDTO> getProductBySku(@PathVariable("sku") final String sku) {
        return restTemplate.getForObject("http://PRODUCTACMEAPPLICATION/products/" + sku, ResponseEntity.class);
    }

    @GetMapping(value = "/products/designation/{designation}")
    public ResponseEntity<Iterable<ProductDTO>> findAllByDesignation(@PathVariable("designation") final String designation){
        return restTemplate.getForObject("http://PRODUCTACMEAPPLICATION/products/designation/" + designation, ResponseEntity.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDTO> create(@RequestBody Product manager) {
        HttpEntity<Product> request = new HttpEntity<>(manager);
        return restTemplate.postForObject("http://REVIEWSACMEAPPLICATION/products", request, ResponseEntity.class);
    }

    @PatchMapping(value = "/products/{sku}")
    public ResponseEntity<ProductDTO> Update(@PathVariable("sku") final String sku, @RequestBody final Product product) throws Exception {
        return restTemplate.exchange("http://PRODUCTACMEAPPLICATION/products/" + sku, HttpMethod.PUT, new HttpEntity<>(product), ProductDTO.class);
    }

    @DeleteMapping(value = "/products/{sku}")
    public void delete(@PathVariable("sku") final String sku ) throws Exception {
       restTemplate.delete("http://PRODUCTACMEAPPLICATION/products/" + sku);
    }
}
