package com.example.smartcart.controller;


import com.example.smartcart.dto.CartDto;
import com.example.smartcart.entity.Cart;
import com.example.smartcart.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Cart> addToCart(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CartDto cartDto) {
        Cart cartItem = cartService.addToCart(userDetails.getUsername(), cartDto);
        return new ResponseEntity<>(cartItem, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<List<Cart>> getCartItems(@AuthenticationPrincipal UserDetails userDetails) {
        List<Cart> cartItems = cartService.getCartItems(userDetails.getUsername());
        return ResponseEntity.ok(cartItems);
    }

    @DeleteMapping("/{productId}")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<String> removeCartItem(@AuthenticationPrincipal UserDetails userDetails, @PathVariable Long productId) {
        cartService.removeCartItem(userDetails.getUsername(), productId);
        return ResponseEntity.ok("Item removed from cart successfully.");
    }
    
    @GetMapping("/total")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<Double> getCartTotal(@AuthenticationPrincipal UserDetails userDetails) {
        double total = cartService.getCartTotal(userDetails.getUsername());
        return ResponseEntity.ok(total);
    }
}