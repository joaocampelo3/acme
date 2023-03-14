package com.isep.acme.services.interfaces;

import com.isep.acme.model.DTO.VoteReviewDTO;
import com.isep.acme.services.impl.VoteServiceImpl;

public interface VoteService {

    VoteReviewDTO create(String vote, Long userId);

}
