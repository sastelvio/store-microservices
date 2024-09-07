package com.sastelvio.product_service.service;

import com.sastelvio.product_service.dto.ProductRequest;
import com.sastelvio.product_service.dto.ProductResponse;
import com.sastelvio.product_service.model.Product;
import com.sastelvio.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository repository;

    public void create(ProductRequest request){
        Product model = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .build();

        repository.save(model);
        log.info("Product {} saved!", model.getName());
    }

    public List<ProductResponse> fetchAll() {
        List<Product> products = repository.findAll();

        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product model) {
        return ProductResponse.builder()
                .id(model.getId())
                .name(model.getName())
                .description(model.getDescription())
                .price(model.getPrice())
                .build();
    }
}
