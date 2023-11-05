package com.github.matidominati.medicalclinic.client.model;

import com.github.matidominati.medicalclinic.client.model.Product;
import lombok.Data;

import java.util.List;

@Data
public class Order {
    private Long id;
    private String customerName;
    private String orderDate;
    private List<Product> products;
}
