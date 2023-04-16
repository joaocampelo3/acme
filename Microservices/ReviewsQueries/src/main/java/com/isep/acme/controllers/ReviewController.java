package com.isep.acme.controllers;

import com.isep.acme.model.DTO.CreateReviewDTO;
import com.isep.acme.model.DTO.ReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.isep.acme.services.interfaces.ReviewService;

import java.util.List;


@Tag(name = "Review", description = "Endpoints for managing Review")
@RestController
class ReviewController {

    @Autowired
    private ReviewService rService;

    @Operation(summary = "finds a product through its sku and shows its review by status")
    @GetMapping("/products/{sku}/reviews/{status}")
    public ResponseEntity<List<ReviewDTO>> findById(@PathVariable(value = "sku") final String sku, @PathVariable(value = "status") final String status) {

        final var review = rService.getReviewsOfProduct(sku, status);

        return ResponseEntity.ok().body( review );
    }

    @Operation(summary = "gets review by user")
    @GetMapping("/reviews/{userID}")
    public ResponseEntity<List<ReviewDTO>> findReviewByUser(@PathVariable(value = "userID") final Long userID) {

        final var review = rService.findReviewsByUser(userID);

        return ResponseEntity.ok().body(review);
    }

    @Operation(summary = "gets pedding reviews")
    @GetMapping("/reviews/pending")
    public ResponseEntity<List<ReviewDTO>> getPendingReview(){

        List<ReviewDTO> r = rService.findPendingReview();

        return ResponseEntity.ok().body(r);
    }
}
