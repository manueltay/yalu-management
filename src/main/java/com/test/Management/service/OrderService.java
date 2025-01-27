package com.test.Management.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.Management.exceptions.EntityNotFoundException;
import com.test.Management.producer.KafkaProducer;
import com.test.Management.repository.CustomerRepository;
import com.test.Management.repository.ProductRepository;
import com.test.Management.repository.PurchaseRepository;
import com.test.Management.sales.model.Customer;
import com.test.Management.sales.model.Product;
import com.test.Management.sales.model.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;
    private final PurchaseRepository purchaseRepository;
    private final KafkaProducer producer;
    ObjectMapper objectMapper = new ObjectMapper();

    public Flux<Product> getAllProducts() {
        return productRepository.findAll()
                .switchIfEmpty(Mono.error(new EntityNotFoundException("There are no products")));
    }

    @Cacheable(cacheNames = "products", key = "#id")
    public Mono<Product> getProductById(@PathVariable String id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Product not found")));
    }

    public Flux<Customer> getAllCostumers() {
        return customerRepository.findAll()
                .switchIfEmpty(Mono.error(new EntityNotFoundException("There are no products")));
    }

    public Mono<Customer> getCustomerById(@PathVariable String id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Customer not found")));
    }

    public Mono<Product> addProduct(@RequestBody Product product) throws JsonProcessingException {
        String jsonString = objectMapper.writeValueAsString(product);
        producer.sendMessage(product.id(), jsonString);
        return productRepository.save(product);
    }

    public Mono<ResponseEntity<Object>> deleteProduct(@PathVariable String id) {
        return productRepository.findById(id)
                .flatMap(existingProduct ->
                        productRepository.delete(existingProduct)
                                .then(Mono.just(ResponseEntity.noContent().build()))
                )
                .switchIfEmpty(Mono.error(new EntityNotFoundException(String.format("Product %s was not found", id))));
    }
}
