package com.test.Management.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.test.Management.exceptions.EntityNotFoundException;
import com.test.Management.sales.model.Customer;
import com.test.Management.sales.model.Product;
import com.test.Management.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderRestController {

    private final OrderService service;

    @GetMapping("/products")
    public Flux<Product> getAllProducts() {
        return service.getAllProducts()
                .switchIfEmpty(Mono.error(new EntityNotFoundException("There are no products")));
    }

    @GetMapping("/products/{id}")
    public Mono<Product> getProductById(@PathVariable String id) {
        return service.getProductById(id);
    }

    @GetMapping("/customers")
    public Flux<Customer> getAllCostumers() {
        return service.getAllCostumers();
    }

    @GetMapping("/customers/{id}")
    public Mono<Customer> getCustomerById(@PathVariable String id) {
        return service.getCustomerById(id);
    }

    @PostMapping("/products")
    public Mono<Product> addProduct(@RequestBody Product product) throws JsonProcessingException {
        return service.addProduct(product);
    }

    @DeleteMapping("/products/{id}")
    public Mono<ResponseEntity<Object>> deleteProduct(@PathVariable String id) {
        return service.deleteProduct(id);
    }
}
