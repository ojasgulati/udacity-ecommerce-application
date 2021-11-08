package com.example.demo;

import com.example.demo.controllers.ItemController;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class ItemControllerMockTests {
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private ItemController itemController;

    @Test
    public void getItems() {
        List<Item> items = new ArrayList<>();
        when(itemRepository.findAll()).thenReturn(items);
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(items, responseEntity.getBody());
    }

    @Test
    public void getExistentItemById() {
        Item item = new Item();
        Long id = 1L;
        item.setId(id);
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        ResponseEntity<Item> responseEntity = itemController.getItemById(id);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(item.getId(), responseEntity.getBody().getId());
    }

    @Test
    public void getNonExistentItemById() {
        when(itemRepository.findById(any())).thenReturn(Optional.empty());
        ResponseEntity<Item> responseEntity = itemController.getItemById(1L);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void getItemsByName() {
        List<Item> expectedList = new ArrayList<>();
        expectedList.add(new Item());
        String name = "bilal";
        when(itemRepository.findByName(name)).thenReturn(expectedList);
        ResponseEntity<List<Item>> responseEntity = itemController.getItemsByName(name);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedList, responseEntity.getBody());
    }
}
