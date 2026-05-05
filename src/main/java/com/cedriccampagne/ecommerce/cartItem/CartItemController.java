package com.cedriccampagne.ecommerce.cartItem;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cedriccampagne.ecommerce.cartItem.dto.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/cart-items")
public class CartItemController {
    
    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService){
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public List<CartItemDto> getAllCartItems(){
        return cartItemService.getAllCartItems();
    }

    @GetMapping("/{id}")
    public CartItemDto getCartItemById(@PathVariable Long id){
        return cartItemService.getCartItemById(id);
    }

    @GetMapping("/user/{id}")
    public List<CartItemDto> getCartItemsByUserId(@PathVariable Long id) {
        return cartItemService.getCartItemsByUserId(id);
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CartItemDto createCartItem(@Valid @RequestBody CartItemCreateDto dto){
        return cartItemService.createCartItem(dto);
    }

    @PatchMapping("/{id}")
    public CartItemDto updateCartItem(
        @PathVariable Long id,
        @Valid @RequestBody CartItemUpdateDto dto
    ) {
        return cartItemService.updateCartItem(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCartItem(@PathVariable Long id){
        cartItemService.deleteCartItem(id);
    }


}
