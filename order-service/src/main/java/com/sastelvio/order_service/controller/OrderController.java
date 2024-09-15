package com.sastelvio.order_service.controller;

import com.sastelvio.order_service.dto.OrderRequest;
import com.sastelvio.order_service.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest request){
        return CompletableFuture.supplyAsync(()-> service.placeOrder(request));
    }

    public CompletableFuture<String> fallbackMethod(OrderRequest request, RuntimeException exception){
        return CompletableFuture.supplyAsync(()-> "Couldn't complete the order, please try again later!");
    }
}
