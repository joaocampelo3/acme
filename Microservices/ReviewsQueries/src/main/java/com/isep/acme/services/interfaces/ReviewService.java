package com.isep.acme.services.interfaces;

import java.util.List;

import com.isep.acme.model.*;
import com.isep.acme.model.DTO.CreateReviewDTO;
import com.isep.acme.model.DTO.ReviewDTO;
import com.isep.acme.repositories.ProductRepository;

public interface ReviewService {

    Iterable<Review> getAll();

    String createProduct(Product product) throws Exception;

    List<ReviewDTO> getReviewsOfProduct(String sku, String status);

    ReviewDTO create(CreateReviewDTO createReviewDTO, String sku) throws Exception;

    Double getWeightedAverage(String sku);

    Boolean DeleteReview(Long reviewId) throws Exception;

    List<ReviewDTO> findPendingReview();

    ReviewDTO findByReviewID(Long reviewID);

    ReviewDTO moderateReview(Long reviewID, String approved);

    List<ReviewDTO> findReviewsByUser(Long userID);

    Boolean DeleteProduct(String sku) throws Exception;
}
