package com.example.smartcart.service;


import com.example.smartcart.dto.CartDto;
import com.example.smartcart.entity.Cart;

import java.util.List;

public interface CartService {
    Cart addToCart(String username, CartDto cartDto);
    List<Cart> getCartItems(String username);
    void removeCartItem(String username, Long productId);
}