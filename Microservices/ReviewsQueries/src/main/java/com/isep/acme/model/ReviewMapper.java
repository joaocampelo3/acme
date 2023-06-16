package com.isep.acme.model;

import com.isep.acme.model.DTO.ReviewDTO;

import java.util.ArrayList;
import java.util.List;

public class ReviewMapper {

    public static ReviewDTO toDto(Review review){
        return new ReviewDTO(review.getIdReview(), review.getReviewUuid(), review.getReviewText(), review.getPublishingDate(), review.getApprovalStatus(), review.getFunFact(), review.getRating().getRate(), review.getUpVotes(), review.getDownVotes());
    }

    public static List<ReviewDTO> toDtoList(List<Review> review) {
        List<ReviewDTO> dtoList = new ArrayList<>();

        for (Review rev: review) {
            dtoList.add(toDto(rev));
        }
        return dtoList;
    }
}
