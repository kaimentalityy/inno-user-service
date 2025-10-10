package com.innowise.userservice.service;

/**
 * Generic CRUD service interface.
 *
 * @param <T>  DTO type returned by the service
 * @param <ID> Type of the entity identifier (e.g., Long)
 */
public interface CrudService<T, ID> {

    /**
     * Creates a new entity.
     *
     * @param createDto DTO with data to create
     * @return the created DTO
     */
    T create(T createDto);

    /**
     * Updates an existing entity by ID.
     *
     * @param id        ID of the entity to update
     * @param updateDto DTO with updated data
     * @return the updated DTO
     */
    T update(ID id, T updateDto);

    /**
     * Deletes an entity by ID.
     *
     * @param id ID of the entity to delete
     */
    void delete(ID id);

    /**
     * Finds an entity by ID.
     *
     * @param id ID of the entity to find
     * @return the found DTO
     */
    T findById(ID id);
}
