package com.example.demo;

import com.example.demo.controllers.CartController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CartControllerMockTests {
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private CartRepository mockCartRepository;
    @Mock
    private ItemRepository mockItemRepository;
    @InjectMocks
    private CartController cartController;

    private String username = "bilal";

    @Test
    public void addToCartForNonExistentUser() {
        when(mockUserRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(new ModifyCartRequest());
        HttpStatus expected = HttpStatus.NOT_FOUND;
        HttpStatus actual = responseEntity.getStatusCode();
        assertEquals(expected, actual);
    }

    @Test
    public void addNonExistentItemToCart() {
        when(mockUserRepository.findByUsername(username)).thenReturn(new User());
        when(mockItemRepository.findById(any())).thenReturn(Optional.empty());
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(username);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(request);
        HttpStatus expected = HttpStatus.NOT_FOUND;
        HttpStatus actual = responseEntity.getStatusCode();
        assertEquals(expected, actual);
    }

    @Test
    public void addItemToCart() {
        User user = new User();
        user.setCart(new Cart());
        when(mockUserRepository.findByUsername(username)).thenReturn(user);
        Item item = new Item();
        item.setId(1L);
        item.setPrice(BigDecimal.TEN);
        when(mockItemRepository.findById(any())).thenReturn(Optional.of(item));
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(username);
        request.setItemId(item.getId());
        request.setQuantity(1);
        ResponseEntity<Cart> responseEntity = cartController.addTocart(request);
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = responseEntity.getStatusCode();
        assertEquals(expected, actual);
    }

    @Test
    public void removeFromCartForNonExistentUser() {
        when(mockUserRepository.findByUsername(any())).thenReturn(null);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(new ModifyCartRequest());
        HttpStatus expected = HttpStatus.NOT_FOUND;
        HttpStatus actual = responseEntity.getStatusCode();
        assertEquals(expected, actual);
    }

    @Test
    public void removeNonExistentItemFromCart() {
        when(mockUserRepository.findByUsername(username)).thenReturn(new User());
        when(mockItemRepository.findById(any())).thenReturn(Optional.empty());
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(username);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(request);
        HttpStatus expected = HttpStatus.NOT_FOUND;
        HttpStatus actual = responseEntity.getStatusCode();
        assertEquals(expected, actual);
    }

    @Test
    public void removeItemFromCart() {
        User user = new User();
        user.setCart(new Cart());
        when(mockUserRepository.findByUsername(username)).thenReturn(user);
        when(mockItemRepository.findById(any())).thenReturn(Optional.of(new Item()));
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername(username);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(request);
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = responseEntity.getStatusCode();
        assertEquals(expected, actual);
    }
}
