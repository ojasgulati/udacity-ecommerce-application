package com.example.demo;

import com.example.demo.controllers.OrderController;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class OrderControllerMockTests {
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderController orderController;

    private String username = "ojas";

    @Test
    public void SubmitOrderForNonExistentUser() {
        when(mockUserRepository.findByUsername(username)).thenReturn(null);
        ResponseEntity<UserOrder> responseEntity = orderController.submit(username);
        HttpStatus expected = HttpStatus.NOT_FOUND;
        HttpStatus actual = responseEntity.getStatusCode();
        assertEquals(expected, actual);
    }

    @Test
    public void SubmitOrderForExistentUser() {
        User user = new User();
        user.setCart(new Cart());
        when(mockUserRepository.findByUsername(username)).thenReturn(user);
        ResponseEntity<UserOrder> responseEntity = orderController.submit(username);
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = responseEntity.getStatusCode();
        assertEquals(expected, actual);
    }

    @Test
    public void GetOrdersForNonExistentUser() {
        when(mockUserRepository.findByUsername(username)).thenReturn(null);
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(username);
        HttpStatus expected = HttpStatus.NOT_FOUND;
        HttpStatus actual = responseEntity.getStatusCode();
        assertEquals(expected, actual);
    }

    @Test
    public void GetOrdersForExistentUser() {
        when(mockUserRepository.findByUsername(username)).thenReturn(new User());
        ResponseEntity<List<UserOrder>> responseEntity = orderController.getOrdersForUser(username);
        HttpStatus expected = HttpStatus.OK;
        HttpStatus actual = responseEntity.getStatusCode();
        assertEquals(expected, actual);
    }
}
