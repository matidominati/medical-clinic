package com.github.matidominati.medicalclinic.client.model;

import lombok.Data;

@Data
public class Product {
    private Long id;
    private String name;
    private String description;
    private double price;
}
