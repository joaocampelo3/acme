package com.isep.acme.services.impl;

import com.isep.acme.model.DTO.VoteReviewDTO;
import com.isep.acme.model.VoteTemp;
import com.isep.acme.repositories.VoteTempRepository;
import com.isep.acme.services.interfaces.VoteListenerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class VotesListenerServiceImpl implements VoteListenerService {
    private static final Logger LOGGER = Logger.getLogger(VotesListenerServiceImpl.class.getName());
    @Autowired
    private final VoteTempRepository voteTempRepository;


    /*private final static String EXCHANGE_NAME = "votes";
    private final static String QUEUE_NAME = "votes";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }*/

    public void handleMessage(String message) {
        LOGGER.log(Level.INFO, " [x] Received '" + message + "'");
        VoteReviewDTO voteReviewDTO = VoteReviewDTO.fromJson(message);
        LOGGER.log(Level.INFO, "Trying to save vote without review...");
        voteTempRepository.save(new VoteTemp(voteReviewDTO.getVoteID(), voteReviewDTO.getVoteTempID(),
                voteReviewDTO.getVote(), voteReviewDTO.getUserID(), voteReviewDTO.getReview()));
    }
}
