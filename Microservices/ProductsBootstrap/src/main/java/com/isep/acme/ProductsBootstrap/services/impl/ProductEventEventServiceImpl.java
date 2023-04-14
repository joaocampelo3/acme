package com.isep.acme.ProductsBootstrap.services.impl;

import com.isep.acme.ProductsBootstrap.model.EventType;
import com.isep.acme.ProductsBootstrap.model.ProductEvent;
import com.isep.acme.ProductsBootstrap.repository.ProductEventRepo;
import com.isep.acme.ProductsBootstrap.services.interfaces.ProductEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Component
public class ProductEventEventServiceImpl implements ProductEventService {

    @Autowired
    private ProductEventRepo productEventRepo;

    @Override
    public List<ProductEvent> getAllProducts() {
        return productEventRepo.findAll();
    }

    @Override
    public ProductEvent addProductEvent(ProductEvent productEvent) {
        boolean flag = false;
        for (ProductEvent p : getAllProducts()) {
            if (p.getSku() == productEvent.getSku() && p.getEventType() == EventType.DELETE) {
                flag = true;
                break;
            }
        }
        if (flag) {
            throw new RuntimeException("Product does not exists");
        }
        return productEventRepo.save(productEvent);
    }

}