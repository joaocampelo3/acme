package com.isep.acme.repositories;

import com.isep.acme.model.Review;
import com.isep.acme.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends CrudRepository<Review , Long> {
    @Query("SELECT r FROM Review r WHERE r.idReview=:reviewID")
    Optional<Review> findByReviewID(Long reviewID);

}
