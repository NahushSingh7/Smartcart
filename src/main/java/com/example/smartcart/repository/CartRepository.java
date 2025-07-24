package com.example.smartcart.repository;

import com.example.smartcart.entity.Cart;
import com.example.smartcart.entity.Product;
import com.example.smartcart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    // Find all cart items for a specific user
    List<Cart> findByUser(User user);

    // Find a specific product in a user's cart
    Optional<Cart> findByUserAndProduct(User user, Product product);
}