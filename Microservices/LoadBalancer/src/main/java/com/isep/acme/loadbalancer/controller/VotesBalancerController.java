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
    public List<VoteDTO> getAll() {
        return restTemplate.getForObject("http://VOTEQUERIESACMEAPPLICATION/votes", List.class);
    }

    @GetMapping(value = "/votes/{voteID}")
    public VoteDTO findByVoteID(@PathVariable("voteID") final UUID voteID) {
        return restTemplate.getForObject("http://VOTEQUERIESACMEAPPLICATION/votes/" + voteID, VoteDTO.class);
    }

    @PostMapping("/review/{reviewUuid}/votes")
    public VoteDTO create(@PathVariable(value = "reviewUuid") final UUID reviewUuid, @RequestBody VoteDTO voteDTO) throws Exception{
        HttpEntity<VoteDTO> request = new HttpEntity<>(voteDTO);
        return restTemplate.postForObject("http://VOTEACMEAPPLICATION/review/" + reviewUuid + "/votes", request, VoteDTO.class);
    }

    @PostMapping("/noreview/{sku}/votes")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public VoteTempDTO createTemp(@PathVariable(value = "sku") final String sku, @RequestBody VoteTempDTO voteTempDTO) throws Exception{
        HttpEntity<VoteTempDTO> request = new HttpEntity<>(voteTempDTO);
        return restTemplate.postForObject("http://VOTEACMEAPPLICATION/noreview/" + sku + "/votes", request, VoteTempDTO.class);
    }


    @PatchMapping(value = "/votes/{voteID}")
    public VoteDTO Update(@PathVariable("voteID") final UUID voteID, @RequestBody final Vote vote) throws Exception {
        return restTemplate.exchange("http://VOTEACMEAPPLICATION/votes/"+ voteID, HttpMethod.PUT, new HttpEntity<>(vote), VoteDTO.class).getBody();
    }


    @DeleteMapping(value = "/votes/{voteID}")
    public void delete(@PathVariable("voteID") final UUID voteID) throws Exception {
        restTemplate.delete("http://VOTEACMEAPPLICATION/votes/" + voteID);
    }

}
