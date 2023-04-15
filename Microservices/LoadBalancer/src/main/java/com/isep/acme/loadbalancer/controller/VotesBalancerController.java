package com.isep.acme.loadbalancer.controller;

import com.isep.acme.loadbalancer.model.DTO.CreateReviewDTO;
import com.isep.acme.loadbalancer.model.DTO.ReviewDTO;
import com.isep.acme.loadbalancer.model.DTO.VoteDTO;
import com.isep.acme.loadbalancer.model.DTO.VoteTempDTO;
import com.isep.acme.loadbalancer.model.Vote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class VotesBalancerController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/votes")
    public ResponseEntity<Iterable<VoteDTO>> getAll() {
        return restTemplate.getForObject("http://VOTEACMEAPPLICATION/votes", ResponseEntity.class);
    }

    @GetMapping(value = "/votes/{voteID}")
    public ResponseEntity<VoteDTO> findByVoteID(@PathVariable("voteID") final UUID voteID) {
        return restTemplate.getForObject("http://VOTEACMEAPPLICATION/votes/" + voteID, ResponseEntity.class);
    }

    @PostMapping("/review/{reviewID}/votes/")
    public ResponseEntity<VoteDTO> create(@PathVariable(value = "reviewID") final Long reviewID, @RequestBody VoteDTO voteDTO) throws Exception{
        HttpEntity<VoteDTO> request = new HttpEntity<>(voteDTO);
        return restTemplate.postForObject("http://REVIEWSACMEAPPLICATION/review/" + reviewID + "/votes/", request, ResponseEntity.class);
    }

    @PostMapping("/noreview/{sku}/votes/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<VoteTempDTO> createTemp(@PathVariable(value = "sku") final String sku, @RequestBody VoteTempDTO voteTempDTO) throws Exception{
        HttpEntity<VoteTempDTO> request = new HttpEntity<>(voteTempDTO);
        return restTemplate.postForObject("http://REVIEWSACMEAPPLICATION/noreview/" + sku + "/votes/", request, ResponseEntity.class);
    }


    @PatchMapping(value = "/votes/{voteID}")
    public ResponseEntity<VoteDTO> Update(@PathVariable("voteID") final UUID voteID, @RequestBody final Vote vote) throws Exception {
        return restTemplate.exchange("http://REVIEWSACMEAPPLICATION/votes/"+ voteID, HttpMethod.PUT, new HttpEntity<>(vote), VoteDTO.class);
    }


    @DeleteMapping(value = "/votes/{voteID}")
    public void delete(@PathVariable("voteID") final UUID voteID) throws Exception {
        restTemplate.delete("http://REVIEWSACMEAPPLICATION/votes/" + voteID);
    }

}
