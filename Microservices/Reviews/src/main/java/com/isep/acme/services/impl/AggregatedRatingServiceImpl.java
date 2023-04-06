package com.isep.acme.services.impl;

import com.isep.acme.model.AggregatedRating;
import com.isep.acme.repositories.AggregatedRatingRepository;
import com.isep.acme.services.interfaces.AggregatedRatingService;
import com.isep.acme.services.interfaces.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AggregatedRatingServiceImpl implements AggregatedRatingService {

    @Autowired
    AggregatedRatingRepository arRepository;

    @Autowired
    ReviewService rService;

    @Override
    public AggregatedRating save(String skuIn) {

        Optional<String> sku = arRepository.findSku(skuIn);

        if (sku.isEmpty()) {
            return null;
        }

        Double average = rService.getWeightedAverage(skuIn);


        Optional<AggregatedRating> r = arRepository.findBySku(skuIn);
        AggregatedRating aggregateF;

        if (r.isPresent()) {
            r.get().setAverage(average);
            aggregateF = arRepository.save(r.get());
        } else {
            AggregatedRating f = new AggregatedRating(average, skuIn);
            aggregateF = arRepository.save(f);
        }

        return aggregateF;
    }


}
