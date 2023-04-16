package com.isep.acme.loadbalancer.controller;

import com.isep.acme.loadbalancer.model.DTO.CreateReviewDTO;
import com.isep.acme.loadbalancer.model.DTO.ReviewDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class ReviewsBalancerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/products/{sku}/reviews/{status}")
    public List<ReviewDTO> findById(@PathVariable(value = "sku") final String sku, @PathVariable(value = "status") final String status) {
        return restTemplate.getForObject("http://REVIEWSQUERIESACMEAPPLICATION/products/" + sku + "/reviews/" + status, List.class);
    }

    @GetMapping("/reviews/{userID}")
    public List<ReviewDTO> findReviewByUser(@PathVariable(value = "userID") final Long userID) {
        return restTemplate.getForObject("http://REVIEWSQUERIESACMEAPPLICATION/reviews/" + userID, List.class);
    }

    @GetMapping("/reviews/pending")
    public List<ReviewDTO> getPendingReview(){
        return restTemplate.getForObject("http://REVIEWSQUERIESACMEAPPLICATION/reviews/pending", List.class);
    }

    @PostMapping("/products/{sku}/reviews")
    public ReviewDTO createReview(@PathVariable(value = "sku") final String sku, @RequestBody CreateReviewDTO createReviewDTO) throws Exception {
        HttpEntity<CreateReviewDTO> request = new HttpEntity<>(createReviewDTO);
        return restTemplate.postForObject("http://REVIEWSACMEAPPLICATION/products/"+sku+"/review", request, ReviewDTO.class);
    }

    @DeleteMapping("/reviews/{reviewID}")
    public void deleteReview(@PathVariable(value = "reviewID") final Long reviewID) throws Exception {
        restTemplate.delete("http://REVIEWSACMEAPPLICATION/reviews/" + reviewID);
    }

    @PutMapping("/reviews/acceptreject/{reviewID}")
    public ReviewDTO putAcceptRejectReview(@PathVariable(value = "reviewID") final Long reviewID, @RequestBody String approved){
        return restTemplate.exchange("http://REVIEWSACMEAPPLICATION/reviews/acceptreject/"+ reviewID, HttpMethod.PUT, new HttpEntity<>(approved), ReviewDTO.class).getBody();
    }


}
