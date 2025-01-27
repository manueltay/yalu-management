package com.test.Management.sales.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "products")
public record Product(@Id String id, String name, int price, String description) implements Serializable {

}