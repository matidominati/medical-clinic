package com.github.matidominati.medicalclinic.client;

import com.github.matidominati.medicalclinic.client.model.Order;
import com.github.matidominati.medicalclinic.client.model.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "sklepInternetowy", url = "http://sklep-internetowy.com/api")
public interface SklepInternetowyClient {

    @GetMapping("/products")
    List<Product> getAllProducts();

    @GetMapping("/products/{productId}")
    Product getProductDetails(@PathVariable Long productId);

    @PostMapping("/products")
    Product addProduct(@RequestBody Product product);

    @PutMapping("/products/{productId}")
    Product updateProduct(@PathVariable Long productId, @RequestBody Product product);

    @DeleteMapping("/products/{productId}")
    void deleteProduct(@PathVariable Long productId);

    @GetMapping("/orders")
    List<Order> getAllOrders();

    @GetMapping("/orders/{orderId}")
    Order getOrderDetails(@PathVariable Long orderId);

    @PostMapping("/orders")
    Order createOrder(@RequestBody Order order);

    @PutMapping("/orders/{orderId}")
    Order createOrder(@PathVariable Long orderId, @RequestBody Order order);

    @DeleteMapping("/orders/{orderId}")
    void deleteOrder(@PathVariable Long orderId);
}
