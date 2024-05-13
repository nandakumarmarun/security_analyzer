package com.secitriy.analyzer.service;

import com.secitriy.analyzer.domain.UserAttributes;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.secitriy.analyzer.domain.UserAttributes}.
 */
public interface UserAttributesService {
    /**
     * Save a userAttributes.
     *
     * @param userAttributes the entity to save.
     * @return the persisted entity.
     */
    UserAttributes save(UserAttributes userAttributes);

    /**
     * Updates a userAttributes.
     *
     * @param userAttributes the entity to update.
     * @return the persisted entity.
     */
    UserAttributes update(UserAttributes userAttributes);

    /**
     * Partially updates a userAttributes.
     *
     * @param userAttributes the entity to update partially.
     * @return the persisted entity.
     */
    Optional<UserAttributes> partialUpdate(UserAttributes userAttributes);

    /**
     * Get all the userAttributes.
     *
     * @return the list of entities.
     */
    List<UserAttributes> findAll();

    /**
     * Get all the UserAttributes where ApplicationUser is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<UserAttributes> findAllWhereApplicationUserIsNull();

    /**
     * Get all the userAttributes with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<UserAttributes> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" userAttributes.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<UserAttributes> findOne(Long id);

    /**
     * Delete the "id" userAttributes.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
