package com.example.smartcart.service.impl;


import com.example.smartcart.dto.CartDto;
import com.example.smartcart.exception.ResourceNotFoundException;
import com.example.smartcart.entity.Cart;
import com.example.smartcart.entity.Product;
import com.example.smartcart.entity.User;
import com.example.smartcart.repository.CartRepository;
import com.example.smartcart.repository.ProductRepository;
import com.example.smartcart.repository.UserRepository;
import com.example.smartcart.service.CartService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Cart addToCart(String username, CartDto cartDto) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", 0));
        Product product = productRepository.findById(cartDto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", cartDto.getProductId()));

        Optional<Cart> existingCartItem = cartRepository.findByUserAndProduct(user, product);

        if (existingCartItem.isPresent()) {
            Cart cart = existingCartItem.get();
            cart.setQuantity(cart.getQuantity() + cartDto.getQuantity());
            return cartRepository.save(cart);
        } else {
            Cart newCartItem = new Cart();
            newCartItem.setUser(user);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(cartDto.getQuantity());
            return cartRepository.save(newCartItem);
        }
    }

    @Override
    public List<Cart> getCartItems(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", 0));
        return cartRepository.findByUser(user);
    }

    @Override
    public void removeCartItem(String username, Long productId) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", 0));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        Cart cartItem = cartRepository.findByUserAndProduct(user, product)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "productId", productId));
        
        cartRepository.delete(cartItem);
    }
}