package com.innowise.orderservice.service.impl;

import com.innowise.orderservice.dao.repository.ItemRepository;
import com.innowise.orderservice.dao.specification.ItemsSpecifications;
import com.innowise.orderservice.mapper.ItemMapper;
import com.innowise.orderservice.model.dto.ItemDto;
import com.innowise.orderservice.model.entity.Item;
import com.innowise.orderservice.service.ItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    @Transactional
    public ItemDto create(ItemDto createDto) {
        Item item = itemMapper.toEntity(createDto);
        Item saved = itemRepository.save(item);
        return itemMapper.toDto(saved);
    }

    @Override
    @Transactional
    public ItemDto update(Long id, ItemDto updateDto) {
        Item existing = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item with id " + id + " not found"));

        itemMapper.updateEntity(existing, updateDto);
        Item updated = itemRepository.save(existing);
        return itemMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new EntityNotFoundException("Item with id " + id + " not found");
        }
        itemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto findById(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item with id " + id + " not found"));
        return itemMapper.toDto(item);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ItemDto> searchItems(String name, String price, String exactName, Pageable pageable) {
        Specification<Item> spec = Specification.where(null);

        if (name != null) spec = spec.and(ItemsSpecifications.hasName(name));
        if (exactName != null) spec = spec.and(ItemsSpecifications.hasExactName(exactName));
        if (price != null) spec = spec.and(ItemsSpecifications.hasPrice(price));
        return itemRepository.findAll(spec, pageable)
                .map(itemMapper::toDto);
    }
}
