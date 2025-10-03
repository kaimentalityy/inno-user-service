package com.innowise.userservice.service.impl;

import com.innowise.userservice.model.dto.UserDto;
import com.innowise.userservice.service.CrudService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserServiceInterface extends CrudService<UserDto, Long> {

    /**
     * Returns a list of users by their IDs.
     * @param ids list of user IDs
     * @return list of UserDto
     */
    List<UserDto> findByIds(List<Long> ids);

    /**
     * Finds a user by email.
     * @param email user's email
     * @return UserDto
     */
    UserDto findByEmail(String email);

    /**
     * Searches users by optional filters.
     * @param name user's name
     * @param surname user's surname
     * @param email user's email
     * @param pageable pagination
     * @return page of UserDto
     */
    Page<UserDto> searchUsers(String name, String surname, String email, Pageable pageable);
}
