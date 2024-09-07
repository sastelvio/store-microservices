package com.sastelvio.order_service.controller;

import com.sastelvio.order_service.dto.OrderRequest;
import com.sastelvio.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(@RequestBody OrderRequest request){
        service.placeOrder(request);
        return "Order placed Successfully!";
    }
}
