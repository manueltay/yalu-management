package com.test.Management.sales.controller;

import com.test.Management.exceptions.EntityNotFoundException;
import com.test.Management.repository.CustomerRepository;
import com.test.Management.repository.ProductRepository;
import com.test.Management.repository.PurchaseRepository;
import com.test.Management.sales.model.Customer;
import com.test.Management.sales.model.Product;
import com.test.Management.sales.model.Purchase;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final PurchaseRepository purchaseRepository;

    @QueryMapping
    public Mono<Product> productById(@Argument String id) {
        return productRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Product with ID " + id + " not found")));
    }

    @QueryMapping
    public Mono<Customer> customerById(@Argument String id) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Customer with ID " + id + " not found")));
    }

    @QueryMapping
    public Flux<Purchase> purchaseHistory(@Argument String customerId) {
        return purchaseRepository.findAllByCustomerId(customerId);
    }

    @MutationMapping
    public Mono<Customer> addCustomer(@Argument String name, @Argument String address, @Argument boolean active) {
        Customer customer = new Customer(null, name, address, active);
        return customerRepository.save(customer);
    }

    @MutationMapping
    public Mono<Product> addProduct(@Argument String name, @Argument String description, @Argument int price) {
        Product product = new Product(null, name, price, description);
        return productRepository.save(product);
    }

    @MutationMapping
    public Mono<Purchase> addPurchase(@Argument String customerId, @Argument String productId, @Argument int quantity) {
        Purchase purchase = new Purchase(null, customerId, productId, quantity, LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));
        return purchaseRepository.save(purchase);
    }

    @SchemaMapping
    public Mono<Customer> customer(Purchase purchase) {
        return customerRepository.findById(purchase.customerId())
                .switchIfEmpty(Mono.empty());
    }

    @SchemaMapping
    public Mono<Product> product(Purchase purchase) {
        return productRepository.findById(purchase.productId())
                .switchIfEmpty(Mono.empty());
    }
}
