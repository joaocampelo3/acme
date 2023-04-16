package com.isep.acme.services.impl;

import com.isep.acme.events.EventTypeEnum;
import com.isep.acme.events.ProductEvent;
import com.isep.acme.model.Product;
import com.isep.acme.model.DTO.ProductDTO;
import com.isep.acme.model.DTO.ProductDetailDTO;
import com.isep.acme.repositories.ProductRepository;

import com.isep.acme.services.MBCommunication.Publisher;
import com.isep.acme.services.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository repository;

    @Autowired
    Publisher publisher;

    @Override
    public Optional<Product> getProductBySku( final String sku ) {

        return repository.findBySku(sku);
    }

    @Override
    public Optional<ProductDTO> findBySku(String sku) {
        final Optional<Product> product = repository.findBySku(sku);

        if( product.isEmpty() )
            return Optional.empty();
        else
            return Optional.of( product.get().toDto() );
    }


    @Override
    public Iterable<ProductDTO> findByDesignation(final String designation) {
        Iterable<Product> p = repository.findByDesignation(designation);
        List<ProductDTO> pDto = new ArrayList();
        for (Product pd:p) {
            pDto.add(pd.toDto());
        }

        return pDto;
    }

    @Override
    public Iterable<ProductDTO> getCatalog() {
        Iterable<Product> p = repository.findAll();
        List<ProductDTO> pDto = new ArrayList();
        for (Product pd:p) {
            pDto.add(pd.toDto());
        }

        return pDto;
    }

    public ProductDetailDTO getDetails(String sku) {

        Optional<Product> p = repository.findBySku(sku);

        if (p.isEmpty())
            return null;
        else
            return new ProductDetailDTO(p.get().getSku(), p.get().getDesignation(), p.get().getDescription());
    }


    @Override
    public ProductDTO create(final Product product) throws Exception {

        final Product p = new Product(product.getSku(), product.getDesignation(), product.getDescription());

        ProductDTO productDTO = repository.save(p).toDto();
        publisher.mainPublish(new ProductEvent(p.getSku(), p.getDesignation(), p.getDescription(), EventTypeEnum.CREATE), "product.product_created");

        return productDTO;
    }

    @Override
    public ProductDTO updateBySku(String sku, Product product) throws Exception {
        
        final Optional<Product> productToUpdate = repository.findBySku(sku);

        if( productToUpdate.isEmpty() ) return null;

        productToUpdate.get().updateProduct(product);

        ProductDTO productUpdatedDto = repository.save(productToUpdate.get()).toDto();

        publisher.mainPublish(new ProductEvent(productUpdatedDto.getSku(), productUpdatedDto.getDesignation(), productUpdatedDto.getDescription(), EventTypeEnum.UPDATE), "product.product_updated");

        return productUpdatedDto;
    }

    @Override
    public void deleteBySku(String sku) throws Exception {

        repository.deleteBySku(sku);

        publisher.mainPublish(new ProductEvent(sku, EventTypeEnum.DELETE), "product.product_deleted");

    }
}
