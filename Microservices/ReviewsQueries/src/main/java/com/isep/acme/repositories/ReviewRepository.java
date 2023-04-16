package com.isep.acme.repositories;

import com.isep.acme.model.Product;
import com.isep.acme.model.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.isep.acme.model.Review;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends CrudRepository<Review , Long> {

    @Query("SELECT r FROM Review r WHERE r.approvalStatus='pending'")
    Optional<List<Review>> findPendingReviews();

    @Query("SELECT r FROM Review r WHERE r.approvalStatus='active'")
    Optional<List<Review>> findActiveReviews();

    @Query("SELECT r FROM Review r WHERE r.sku=:sku AND r.approvalStatus=:status ORDER BY r.publishingDate DESC")
    Optional<List<Review>> findBySkuStatus(String sku, String status);

    @Query("SELECT r FROM Review r WHERE r.user=:user ORDER BY r.publishingDate DESC")
    Optional<List<Review>> findByUserId(User user);


    @Query("SELECT r FROM Review r, Product p WHERE r.sku=p.sku AND p.sku=:sku")
    Optional<String> findBySku(String sku);

    @Query("SELECT r FROM Review r, Product p WHERE r.sku=p.sku AND p.sku=:sku")
    Optional<List<Review>> findBySkuList(String sku);

    @Query("SELECT r FROM Review r WHERE r.idReview=:reviewID")
    Review findByReviewID(Long reviewID);
}
