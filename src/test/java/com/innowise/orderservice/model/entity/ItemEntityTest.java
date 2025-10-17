package com.innowise.orderservice.model.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ItemEntityTest {

    @Test
    void testGettersAndSetters() {
        Item item = new Item();

        item.setId(1L);
        item.setName("Laptop");
        item.setPrice(new BigDecimal("999.99"));

        assertEquals(1L, item.getId());
        assertEquals("Laptop", item.getName());
        assertEquals(new BigDecimal("999.99"), item.getPrice());
    }

    @Test
    void testNullValues() {
        Item item = new Item();
        item.setName(null);
        item.setPrice(null);

        assertNull(item.getName());
        assertNull(item.getPrice());
    }

    @Test
    void testPriceUpdate() {
        Item item = new Item();
        item.setPrice(new BigDecimal("100.00"));
        assertEquals(new BigDecimal("100.00"), item.getPrice());

        item.setPrice(new BigDecimal("150.50"));
        assertEquals(new BigDecimal("150.50"), item.getPrice());
    }

    @Test
    void testNameUpdate() {
        Item item = new Item();
        item.setName("Phone");
        assertEquals("Phone", item.getName());

        item.setName("Tablet");
        assertEquals("Tablet", item.getName());
    }
}
