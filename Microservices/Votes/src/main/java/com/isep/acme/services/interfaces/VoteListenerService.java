package com.isep.acme.services.interfaces;

import com.isep.acme.model.DTO.VoteDTO;
import com.isep.acme.model.Vote;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import java.util.List;
import java.util.Optional;

import static com.isep.acme.rabbitMQConfigs.RabbitMQMacros.SUBSCRIBE_EXCHANGE_NAME;

public interface VoteListenerService {

    @RabbitListener(queues = SUBSCRIBE_EXCHANGE_NAME, id = "voteCreateWithoutReview")
    void handleMessage(String message);
}
