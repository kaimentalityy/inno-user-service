package com.innowise.orderservice.model.entity;

import com.innowise.orderservice.dao.repository.ItemRepository;
import com.innowise.orderservice.integration.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
class ItemIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;

    private Item item;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();

        item = new Item();
        item.setName("Laptop");
        item.setPrice(BigDecimal.valueOf(999.99));
        item = itemRepository.saveAndFlush(item);
    }

    @Test
    void testItemIsPersistedCorrectly() {
        Optional<Item> savedItemOpt = itemRepository.findById(item.getId());
        assertThat(savedItemOpt).isPresent();

        Item savedItem = savedItemOpt.get();
        assertThat(savedItem.getId()).isNotNull();
        assertThat(savedItem.getName()).isEqualTo("Laptop");
        assertThat(savedItem.getPrice()).isEqualByComparingTo("999.99");
    }

    @Test
    void testItemUpdate() {
        Item savedItem = itemRepository.findById(item.getId()).orElseThrow();
        savedItem.setName("Updated Laptop");
        savedItem.setPrice(BigDecimal.valueOf(1099.99));
        itemRepository.saveAndFlush(savedItem);

        Item updatedItem = itemRepository.findById(item.getId()).orElseThrow();
        assertThat(updatedItem.getName()).isEqualTo("Updated Laptop");
        assertThat(updatedItem.getPrice()).isEqualByComparingTo("1099.99");
    }

    @Test
    void testItemDeletion() {
        Item savedItem = itemRepository.findById(item.getId()).orElseThrow();
        itemRepository.delete(savedItem);
        itemRepository.flush();

        assertThat(itemRepository.findById(item.getId())).isEmpty();
    }
}
