package com.sastelvio.product_service.controller;

import com.sastelvio.product_service.dto.ProductRequest;
import com.sastelvio.product_service.dto.ProductResponse;
import com.sastelvio.product_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ProductRequest request){
        service.create(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> fetchAll(){
        return service.fetchAll();
    }
}
