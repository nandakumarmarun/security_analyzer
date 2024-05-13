package com.secitriy.analyzer.service;

import com.secitriy.analyzer.domain.District;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.secitriy.analyzer.domain.District}.
 */
public interface DistrictService {
    /**
     * Save a district.
     *
     * @param district the entity to save.
     * @return the persisted entity.
     */
    District save(District district);

    /**
     * Updates a district.
     *
     * @param district the entity to update.
     * @return the persisted entity.
     */
    District update(District district);

    /**
     * Partially updates a district.
     *
     * @param district the entity to update partially.
     * @return the persisted entity.
     */
    Optional<District> partialUpdate(District district);

    /**
     * Get all the districts.
     *
     * @return the list of entities.
     */
    List<District> findAll();

    /**
     * Get all the District where UserAttributes is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<District> findAllWhereUserAttributesIsNull();

    /**
     * Get all the districts with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<District> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" district.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<District> findOne(Long id);

    /**
     * Delete the "id" district.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
