package com.sastelvio.order_service.service;

import com.sastelvio.order_service.dto.InventoryResponse;
import com.sastelvio.order_service.dto.OrderLineItemsDto;
import com.sastelvio.order_service.dto.OrderRequest;
import com.sastelvio.order_service.event.OrderPlacedEvent;
import com.sastelvio.order_service.model.Order;
import com.sastelvio.order_service.model.OrderLineItems;
import com.sastelvio.order_service.repository.OrderRepository;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository repository;
    private final KafkaTemplate<String, OrderPlacedEvent> kafka;

    @Autowired
    private WebClient.Builder webClientBuilder;

    public String placeOrder(OrderRequest request){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = request.getOrderLineItemsListDto()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList()
                .stream()
                .map(OrderLineItems::getSkuCode)
                .toList();

        //CHECK THE STOCK USING INVENTORY SERVICE, IF THE PRODUCT IS IN STOCK, PLACE THE ORDER
        //SYNC REQUEST
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                .uri(
                        "http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder
                                .queryParam("skuCode", skuCodes)
                                .build()
                )
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        boolean allProductsInStock = Arrays.stream(Objects.requireNonNull(inventoryResponses))
                .allMatch(InventoryResponse::isInStock);

        if(allProductsInStock){
            repository.save(order);
            kafka.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
            return "Order placed Successfully!";
        }else{
            throw new IllegalArgumentException("Product doesn't have stock!");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQualtity(orderLineItemsDto.getQualtity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;

    }
}
