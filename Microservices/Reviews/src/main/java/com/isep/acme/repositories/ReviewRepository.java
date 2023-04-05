package com.isep.acme.repositories;

import com.isep.acme.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.isep.acme.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends CrudRepository<Review, Long> {

    //Optional<Review> findById(Long productId);

    @Query("SELECT r FROM Review r WHERE r.sku=:sku ORDER BY r.publishingDate DESC")
    Optional<List<Review>> findBySky(String sku);

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
}
