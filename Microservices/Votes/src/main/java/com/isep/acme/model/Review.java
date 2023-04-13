package com.isep.acme.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Review {

    @Id
    private Long idReview;

    public Review(Long idReview) {
        this.idReview = idReview;
    }

    public Review() {

    }

    public Long getIdReview() {
        return idReview;
    }

    public void setIdReview(Long idReview) {
        this.idReview = idReview;
    }
}
