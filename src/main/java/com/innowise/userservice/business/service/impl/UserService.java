package com.innowise.userservice.business.service.impl;

import com.innowise.userservice.business.mapper.UserMapper;
import com.innowise.userservice.business.service.CrudService;
import com.innowise.userservice.data.entity.User;
import com.innowise.userservice.data.repository.UserRepository;
import com.innowise.userservice.data.specification.UserSpecifications;
import com.innowise.userservice.presentation.dto.UserDto;
import com.innowise.userservice.util.exceptions.notfound.EntityNotFoundException;
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

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "users")
public class UserService implements CrudService<UserDto, Long> {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto create(UserDto dto) {
        User user = userMapper.toUser(dto);
        User saved = userRepository.save(user);
        return userMapper.toUserDto(saved);
    }

    @Override
    @CachePut(key = "#id")
    @Transactional
    public UserDto update(Long id, UserDto dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", id));
        userMapper.updateUserFromDto(dto, user);
        return userMapper.toUserDto(userRepository.save(user));
    }

    @Override
    @CacheEvict(key = "#id")
    @Transactional
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Cacheable(key = "#id")
    public UserDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", "id", id));
        return userMapper.toUserDto(user);
    }

    public Page<UserDto> searchUsers(String name, String surname, String email, Pageable pageable) {
        Specification<User> spec = Specification.where(null);

        if (name != null) {
            spec = spec.and(UserSpecifications.hasName(name));
        }
        if (surname != null) {
            spec = spec.and(UserSpecifications.hasSurname(surname));
        }
        if (email != null) {
            spec = spec.and(UserSpecifications.hasEmail(email));
        }

        return userRepository.findAll(spec, pageable)
                .map(userMapper::toUserDto);
    }
}
