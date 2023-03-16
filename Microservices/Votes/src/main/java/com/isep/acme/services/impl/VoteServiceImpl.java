package com.isep.acme.services.impl;

import com.isep.acme.model.DTO.VoteReviewDTO;
import com.isep.acme.services.interfaces.VoteService;
import com.isep.acme.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class VoteServiceImpl implements VoteService {

    UserService userService;

    @Override
    public VoteReviewDTO create(String vote, Long userId) {

        final var user = userService.getUserId(userId);
        if(user.isEmpty()) return null;

        return null;
    }
}
