package com.isep.acme.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.isep.acme.model.AggregatedRating;
import java.util.Optional;

public interface AggregatedRatingRepository extends CrudRepository<AggregatedRating, Long> {

    @Query("SELECT a.sku FROM AggregatedRating a WHERE a.sku=:sku")
    Optional<String> findSku(String sku);

    @Query("SELECT a FROM AggregatedRating a WHERE a.sku=:sku")
    Optional<AggregatedRating> findBySku(String sku);


}
