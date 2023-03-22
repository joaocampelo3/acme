package com.isep.acme.services.interfaces;

import java.util.Optional;

import com.isep.acme.model.Product;
import com.isep.acme.model.DTO.ProductDTO;
import com.isep.acme.model.DTO.ProductDetailDTO;

public interface ProductService {

    Optional<ProductDTO> findBySku(final String sku);

    Optional<Product> getProductBySku( final String sku );

    Iterable<ProductDTO> findByDesignation(final String designation);

    Iterable<ProductDTO> getCatalog();

    ProductDetailDTO getDetails(final String sku);

    ProductDTO create(final Product manager) throws Exception;

    ProductDTO updateBySku(final String sku, final Product product) throws Exception;

    void deleteBySku(final String sku) throws Exception;
}
