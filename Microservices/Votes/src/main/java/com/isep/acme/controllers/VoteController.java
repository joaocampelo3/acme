package com.isep.acme.controllers;

import com.isep.acme.model.DTO.VoteDTO;
import com.isep.acme.model.Vote;
import com.isep.acme.rabbitmqconfigs.RabbitMQHost;
import com.isep.acme.services.interfaces.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static com.isep.acme.rabbitmqconfigs.RabbitMQMacros.EXCHANGE_NAME;

@Tag(name = "Vote", description = "Endpoints for managing  votes")
@RestController
@RequestMapping("/votes")
class VoteController {

    private static final Logger logger = LoggerFactory.getLogger(VoteController.class);
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private VoteService service;

    @Autowired
    private RabbitMQHost rabbitMQHost;

    public VoteController(RabbitMQHost rabbitMQHost) {
        ConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMQHost.getHost(),
                Integer.parseInt(rabbitMQHost.getPort()));
        ((CachingConnectionFactory) connectionFactory).setUsername(rabbitMQHost.getUsername());
        ((CachingConnectionFactory) connectionFactory).setPassword(rabbitMQHost.getPassword());

        this.rabbitTemplate = new RabbitTemplate(connectionFactory);
    }

    @Operation(summary = "gets all votes")
    @GetMapping
    public ResponseEntity<Iterable<VoteDTO>> getAll() {
        final var votes = service.getAll();

        return ResponseEntity.ok().body(votes);
    }

    @Operation(summary = "finds votes by voteID")
    @GetMapping(value = "/{voteID}")
    public ResponseEntity<VoteDTO> findByVoteID(@PathVariable("voteID") final Long voteID) {

        final Optional<VoteDTO> vote = service.findByVoteID(voteID);

        if (vote.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote not found.");
        else
            return ResponseEntity.ok().body(vote.get());
    }

    @Operation(summary = "creates a vote")
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<VoteDTO> create(@RequestBody Vote vote) {
        try {

            final VoteDTO voteDto = service.create(vote);

            rabbitTemplate.convertAndSend(EXCHANGE_NAME, "", voteDto.toJson());

            return new ResponseEntity<VoteDTO>(voteDto, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            logger.error(e.toString());
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Vote must have a unique ID.");
        }
    }

    @Operation(summary = "updates a vote")
    @PatchMapping(value = "/{voteID}")
    public ResponseEntity<VoteDTO> Update(@PathVariable("voteID") final Long voteID, @RequestBody final Vote vote) throws Exception {

        final VoteDTO voteDTO = service.updateByVoteID(voteID, vote);

        if (voteDTO == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vote not found.");
        else
            return ResponseEntity.ok().body(voteDTO);
    }

    @Operation(summary = "deletes a vote")
    @DeleteMapping(value = "/{voteID}")
    public ResponseEntity<Vote> delete(@PathVariable("voteID") final Long voteID) throws Exception {

        service.deleteByVoteID(voteID);
        return ResponseEntity.noContent().build();
    }
}
