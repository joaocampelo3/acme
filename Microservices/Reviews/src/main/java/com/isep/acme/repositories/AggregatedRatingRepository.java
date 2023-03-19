package com.isep.acme.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.isep.acme.model.AggregatedRating;
import java.util.Optional;

public interface AggregatedRatingRepository extends CrudRepository<AggregatedRating, Long> {

    @Query("SELECT a.sku FROM AggregatedRating a WHERE a.sku=:sku")
    Optional<Long> findProductIdBySku(String sku);

    @Query("SELECT a FROM AggregatedRating a WHERE a.productId=:productId")
    Optional<AggregatedRating> findByProductId(Long productId);


}
