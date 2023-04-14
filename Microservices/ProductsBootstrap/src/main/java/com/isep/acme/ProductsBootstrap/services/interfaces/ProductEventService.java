package com.isep.acme.ProductsBootstrap.services.interfaces;

import com.isep.acme.ProductsBootstrap.model.ProductEvent;

import java.util.List;

public interface ProductEventService {
    List<ProductEvent> getAllProducts();

    ProductEvent addProductEvent(ProductEvent productEvent);
}
