package com.secitriy.analyzer.service;

import com.secitriy.analyzer.domain.Location;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.secitriy.analyzer.domain.Location}.
 */
public interface LocationService {
    /**
     * Save a location.
     *
     * @param location the entity to save.
     * @return the persisted entity.
     */
    Location save(Location location);

    /**
     * Updates a location.
     *
     * @param location the entity to update.
     * @return the persisted entity.
     */
    Location update(Location location);

    /**
     * Partially updates a location.
     *
     * @param location the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Location> partialUpdate(Location location);

    /**
     * Get all the locations.
     *
     * @return the list of entities.
     */
    List<Location> findAll();

    /**
     * Get all the Location where UserAttributes is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<Location> findAllWhereUserAttributesIsNull();

    /**
     * Get all the locations with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Location> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" location.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Location> findOne(Long id);

    /**
     * Delete the "id" location.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
