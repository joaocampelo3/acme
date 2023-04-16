package com.isep.acme.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idReview;

    @Column(nullable = false)
    private UUID reviewUuid;

    public Review(UUID reviewUuid) {
        this.reviewUuid = reviewUuid;
    }

    public Review() {

    }

    public Long getIdReview() {
        return idReview;
    }

    public UUID getReviewUuid() {
        return reviewUuid;
    }

    public void setReviewUuid(UUID reviewUuid) {
        this.reviewUuid = reviewUuid;
    }
}
