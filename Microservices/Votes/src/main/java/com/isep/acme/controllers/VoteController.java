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

    @Operation(summary = "creates a vote")
    @PostMapping("/review/{reviewUuid}/votes")
    public ResponseEntity<VoteDTO> create(@PathVariable(value = "reviewUuid") final UUID reviewUuid, @RequestBody VoteDTO voteDTO) throws Exception{
        try {

            final VoteDTO voteDto = service.create(voteDTO, reviewUuid);

            if(voteDto == null){
                return ResponseEntity.badRequest().build();
            }
            else{
                return new ResponseEntity<VoteDTO>(voteDto, HttpStatus.CREATED);
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vote must have a unique ID.");
        }
    }


    @Operation(summary = "creates a temporary vote")
    @PostMapping("/noreview/{sku}/votes")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<VoteTempDTO> createTemp(@PathVariable(value = "sku") final String sku, @RequestBody VoteTempDTO voteTempDTO) throws Exception{
        try {

            final VoteTempDTO voteTempDTOfinal = service.createTemp(voteTempDTO, sku);

            if(voteTempDTOfinal == null){
                return ResponseEntity.badRequest().build();
            }
            else{
                return new ResponseEntity<VoteTempDTO>(voteTempDTOfinal, HttpStatus.ACCEPTED);
            }

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vote must have a unique ID.");
        }
    }

    @Operation(summary = "updates a vote")
    @PatchMapping(value = "/votes/{voteID}")
    public ResponseEntity<VoteDTO> Update(@PathVariable("voteID") final UUID voteID, @RequestBody final Vote vote) throws Exception {

        final VoteDTO voteDTO = service.updateByVoteID(voteID, vote);

        if (voteDTO == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote not found.");
        else
            return ResponseEntity.ok().body(voteDTO);
    }

    @Operation(summary = "deletes a vote")
    @DeleteMapping(value = "/votes/{voteID}")
    public ResponseEntity<Vote> delete(@PathVariable("voteID") final UUID voteID) throws Exception {

        service.deleteByVoteID(voteID);
        return ResponseEntity.noContent().build();
    }
}
