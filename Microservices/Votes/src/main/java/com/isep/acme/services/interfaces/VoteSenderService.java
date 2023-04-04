package com.isep.acme.services.interfaces;

import org.springframework.scheduling.annotation.Scheduled;

public interface VoteSenderService {

    @Scheduled(fixedDelay = 1000, initialDelay = 500)
    void send();
}
