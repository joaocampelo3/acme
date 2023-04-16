package com.isep.acme.controllers;

import com.isep.acme.model.DTO.VoteDTO;
import com.isep.acme.model.DTO.VoteTempDTO;
import com.isep.acme.model.Vote;
import com.isep.acme.services.interfaces.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Tag(name = "Vote", description = "Endpoints for managing  votes")
@RestController
class VoteController {

    @Autowired
    private VoteService service;

    @Operation(summary = "gets all votes")
    @GetMapping("/votes")
    public ResponseEntity<Iterable<VoteDTO>> getAll() {
        final var votes = service.getAll();

        return ResponseEntity.ok().body(votes);
    }

    @Operation(summary = "finds votes by voteID")
    @GetMapping(value = "/votes/{voteID}")
    public ResponseEntity<VoteDTO> findByVoteID(@PathVariable("voteID") final UUID voteID) {

        final Optional<Vote> vote = service.findByVoteID(voteID);

        if (vote.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote not found.");
        else
            return ResponseEntity.ok().body(vote.get().toDto());
    }
}
