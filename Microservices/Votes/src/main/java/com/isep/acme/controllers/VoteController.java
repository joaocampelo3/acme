package com.isep.acme.controllers;

import com.isep.acme.model.DTO.VoteReviewDTO;
import com.isep.acme.model.Vote;
import com.isep.acme.services.interfaces.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Tag(name = "Vote", description = "Endpoints for managing  votes")
@RestController
@RequestMapping("/votes")
class VoteController {

    private static final Logger logger = LoggerFactory.getLogger(VoteController.class);

    @Autowired
    private VoteService service;

    @Operation(summary = "gets all votes")
    @GetMapping
    public ResponseEntity<Iterable<VoteReviewDTO>> getAll() {
        final var votes = service.getAll();

        return ResponseEntity.ok().body( votes );
    }

    @Operation(summary = "finds votes by voteID")
    @GetMapping(value = "/{voteID}")
    public ResponseEntity<VoteReviewDTO> findByVoteID(@PathVariable("voteID") final Long voteID) {

        final Optional<VoteReviewDTO> vote = service.findByVoteID(voteID);

        if( vote.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Vote not found.");
        else
            return ResponseEntity.ok().body(vote.get());
    }

    @Operation(summary = "creates a vote")
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<VoteReviewDTO> create(@RequestBody Vote vote) {
        try {
            final VoteReviewDTO voteDto = service.create(vote);
            return new ResponseEntity<VoteReviewDTO>(voteDto, HttpStatus.ACCEPTED);
        }
        catch( Exception e ) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Vote must have a unique ID.");
        }
    }

    @Operation(summary = "updates a vote")
    @PatchMapping(value = "/{voteID}")
    public ResponseEntity<VoteReviewDTO> Update(@PathVariable("voteID") final Long voteID, @RequestBody final Vote vote) throws Exception {

        final VoteReviewDTO voteDTO = service.updateByVoteID(voteID, vote);

        if( voteDTO == null )
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Vote not found.");
        else
            return ResponseEntity.ok().body(voteDTO);
    }

    @Operation(summary = "deletes a vote")
    @DeleteMapping(value = "/{voteID}")
    public ResponseEntity<Vote> delete(@PathVariable("voteID") final Long voteID ) throws Exception {

        service.deleteByVoteID(voteID);
        return ResponseEntity.noContent().build();
    }
}
