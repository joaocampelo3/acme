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

import java.util.List;

@RestController
public class ProductsBalancerController {

    @Autowired
    private  RestTemplate restTemplate;

    @GetMapping("/products")
    public List<ProductDTO> getCatalog() {
        return restTemplate.getForObject("http://PRODUCTQUERIESACMEAPPLICATION/products", List.class);
    }

    @GetMapping(value = "/products/{sku}")
    public ProductDTO getProductBySku(@PathVariable("sku") final String sku) {
        return restTemplate.getForObject("http://PRODUCTQUERIESACMEAPPLICATION/products/" + sku, ProductDTO.class);
    }

    @GetMapping(value = "/products/designation/{designation}")
    public List<ProductDTO> findAllByDesignation(@PathVariable("designation") final String designation){
        return restTemplate.getForObject("http://PRODUCTQUERIESACMEAPPLICATION/products/designation/" + designation, List.class);
    }

    @PostMapping(value = "/products")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO create(@RequestBody Product manager) {
        HttpEntity<Product> request = new HttpEntity<>(manager);
        return restTemplate.postForObject("http://PRODUCTACMEAPPLICATION/products", request, ProductDTO.class);
    }

    @PatchMapping(value = "/products/{sku}")
    public ProductDTO Update(@PathVariable("sku") final String sku, @RequestBody final Product product) throws Exception {
        return restTemplate.exchange("http://PRODUCTACMEAPPLICATION/products/" + sku, HttpMethod.PUT, new HttpEntity<>(product), ProductDTO.class).getBody();
    }

    @DeleteMapping(value = "/products/{sku}")
    public void delete(@PathVariable("sku") final String sku ) throws Exception {
       restTemplate.delete("http://PRODUCTACMEAPPLICATION/products/" + sku);
    }
}
