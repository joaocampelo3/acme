package com.isep.acme.services.impl;

import com.isep.acme.model.EventTypeEnum;
import com.isep.acme.model.ReviewEvent;
import com.isep.acme.repository.ReviewEventRepo;
import com.isep.acme.services.interfaces.ReviewEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class ReviewEventEventServiceImpl implements ReviewEventService {

    @Autowired
    private ReviewEventRepo reviewEventRepo;

    @Override
    public List<ReviewEvent> getAllReviews() {
        return reviewEventRepo.findAll();
    }
}