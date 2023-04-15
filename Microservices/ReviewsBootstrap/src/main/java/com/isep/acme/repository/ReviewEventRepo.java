package com.isep.acme.repository;

import com.isep.acme.model.ReviewEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewEventRepo extends MongoRepository<ReviewEvent, Long> {
    ReviewEvent findByIdReview(Long idReview);

    List<ReviewEvent> findAll();
}