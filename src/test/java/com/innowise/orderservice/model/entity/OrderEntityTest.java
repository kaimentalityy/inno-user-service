package com.innowise.orderservice.model.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class OrderEntityTest {

    @Test
    void testGettersAndSetters() {
        Order order = new Order();
        order.setId(1L);
        order.setUserId(123L);
        order.setStatus("NEW");
        LocalDateTime now = LocalDateTime.now();
        order.setCreatedDate(now);

        assertEquals(1L, order.getId());
        assertEquals(123L, order.getUserId());
        assertEquals("NEW", order.getStatus());
        assertEquals(now, order.getCreatedDate());
    }

    @Test
    void testItemsList() {
        Order order = new Order();
        assertNotNull(order.getItems());
        assertTrue(order.getItems().isEmpty());

        OrderItem item1 = new OrderItem();
        item1.setId(10L);

        OrderItem item2 = new OrderItem();
        item2.setId(20L);

        order.getItems().add(item1);
        order.getItems().add(item2);

        assertEquals(2, order.getItems().size());
        assertTrue(order.getItems().contains(item1));
        assertTrue(order.getItems().contains(item2));

        // test removal
        order.getItems().remove(item1);
        assertEquals(1, order.getItems().size());
        assertFalse(order.getItems().contains(item1));
    }

    @Test
    void testAddRemoveItemKeepsReference() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setOrder(order);

        order.getItems().add(item);

        assertEquals(order, item.getOrder());
        assertEquals(1, order.getItems().size());

        order.getItems().remove(item);
        assertFalse(order.getItems().contains(item));
    }
}
