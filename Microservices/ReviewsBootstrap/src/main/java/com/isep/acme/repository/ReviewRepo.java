package com.isep.acme.repository;

import com.isep.acme.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepo extends MongoRepository<Review, String> {
    Review findByID(Long id);

    List<Review> findAll();

    void deleteByReviewID(Long idReview);

    boolean existsById(Long id);
}