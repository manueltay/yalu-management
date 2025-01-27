package com.test.Management.sales.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "customers")
public record Customer(@Id String id, String name, String address, boolean active) implements Serializable {


}