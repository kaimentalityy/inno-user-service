package com.innowise.orderservice.service.impl;

import com.innowise.orderservice.dao.repository.ItemRepository;
import com.innowise.orderservice.dao.specification.ItemsSpecifications;
import com.innowise.orderservice.mapper.ItemMapper;
import com.innowise.orderservice.model.dto.ItemDto;
import com.innowise.orderservice.model.entity.Items;
import com.innowise.orderservice.service.CrudService;
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
public class ItemServiceImpl implements CrudService<ItemDto, Long> {

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto create(ItemDto createDto) {
        Items item = itemMapper.toEntity(createDto);
        Items saved = itemRepository.save(item);
        return itemMapper.toDto(saved);
    }

    @Override
    public ItemDto update(Long id, ItemDto updateDto) {
        Items existing = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item with id " + id + " not found"));

        itemMapper.updateEntity(existing, updateDto);
        Items updated = itemRepository.save(existing);
        return itemMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!itemRepository.existsById(id)) {
            throw new EntityNotFoundException("Item with id " + id + " not found");
        }
        itemRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemDto findById(Long id) {
        Items item = itemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item with id " + id + " not found"));
        return itemMapper.toDto(item);
    }

    public Page<ItemDto> searchItems(String name, String price, String exactName, Pageable pageable) {
        Specification<Items> spec = Specification.where(null);

        if (name != null) spec = spec.and(ItemsSpecifications.hasName(name));
        if (exactName != null) spec = spec.and(ItemsSpecifications.hasExactName(exactName));
        if (price != null) spec = spec.and(ItemsSpecifications.hasPrice(price));
        return itemRepository.findAll(spec, pageable)
                .map(itemMapper::toDto);
    }
}
