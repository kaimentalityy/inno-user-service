package com.innowise.orderservice.model.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderItemEntityTest {

    @Test
    void testGettersAndSetters() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(100L);
        orderItem.setQuantity(5);

        Order order = new Order();
        order.setId(1L);

        Item item = new Item();
        item.setId(10L);

        orderItem.setOrder(order);
        orderItem.setItem(item);

        assertEquals(100L, orderItem.getId());
        assertEquals(5, orderItem.getQuantity());
        assertEquals(order, orderItem.getOrder());
        assertEquals(item, orderItem.getItem());
    }

    @Test
    void testNullRelationships() {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(null);
        orderItem.setItem(null);

        assertNull(orderItem.getOrder());
        assertNull(orderItem.getItem());
    }

    @Test
    void testQuantityValidation() {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(1);
        assertEquals(1, orderItem.getQuantity());

        orderItem.setQuantity(100);
        assertEquals(100, orderItem.getQuantity());
    }
}
