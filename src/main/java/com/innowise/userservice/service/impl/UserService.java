package com.innowise.userservice.service.impl;

import com.innowise.userservice.exceptions.notfound.EntityNotFoundException;
import com.innowise.userservice.mapper.UserMapper;
import com.innowise.userservice.model.dto.UserDto;
import com.innowise.userservice.model.entity.User;
import com.innowise.userservice.repository.dao.UserRepository;
import com.innowise.userservice.repository.specification.UserSpecifications;
import com.innowise.userservice.service.UserServiceInterface;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "users")
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final CardInfoService cardInfoService;

    @Override
    @Transactional
    @CachePut(key = "#result.id")
    public UserDto create(UserDto dto) {
        User user = userMapper.toUser(dto);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDto update(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", id));
        userMapper.updateUserFromDto(dto, user);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @Transactional
    @CacheEvict(key = "#id")
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", id));

        user.getCards().forEach(card -> cardInfoService.evictCardFromCache(card.getId()));

        userRepository.delete(user);
    }

    @Override
    @Transactional
    @Cacheable(key = "#id")
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", id));
        return userMapper.toUserDto(user);
    }

    @Transactional
    public List<UserDto> findByIds(List<Long> ids) {
        return userRepository.findAllById(ids)
                .stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    @Transactional
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User", "email", email));
        return userMapper.toUserDto(user);
    }

    @Transactional
    public Page<UserDto> searchUsers(String name, String surname, String email, Pageable pageable) {
        Specification<User> spec = Specification.where(null);
        if (name != null) spec = spec.and(UserSpecifications.hasName(name));
        if (surname != null) spec = spec.and(UserSpecifications.hasSurname(surname));
        if (email != null) spec = spec.and(UserSpecifications.hasEmail(email));
        return userRepository.findAll(spec, pageable).map(userMapper::toUserDto);
    }
}



