package com.test.Management.sales.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;


@Document(collection = "purchases")
public record Purchase(@Id String id, String customerId, String productId, int quantity, String date) implements Serializable {

}
