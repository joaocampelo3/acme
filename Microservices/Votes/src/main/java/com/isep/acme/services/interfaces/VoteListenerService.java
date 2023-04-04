package com.isep.acme.services.interfaces;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static com.isep.acme.rabbitMQConfigs.RabbitMQMacros.SUBSCRIBE_EXCHANGE_NAME;

public interface VoteListenerService {

    @RabbitListener(queues = SUBSCRIBE_EXCHANGE_NAME, id = "voteCreateWithoutReview")
    void handleMessage(String message);
}
