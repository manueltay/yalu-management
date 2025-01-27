package com.test.Management.repository;

import com.test.Management.sales.model.Purchase;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface PurchaseRepository extends ReactiveMongoRepository<Purchase, String> {

    public Flux<Purchase> findAllByCustomerId(String customerId);
}
