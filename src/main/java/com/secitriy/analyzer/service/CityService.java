package com.secitriy.analyzer.service;

import com.secitriy.analyzer.domain.City;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.secitriy.analyzer.domain.City}.
 */
public interface CityService {
    /**
     * Save a city.
     *
     * @param city the entity to save.
     * @return the persisted entity.
     */
    City save(City city);

    /**
     * Updates a city.
     *
     * @param city the entity to update.
     * @return the persisted entity.
     */
    City update(City city);

    /**
     * Partially updates a city.
     *
     * @param city the entity to update partially.
     * @return the persisted entity.
     */
    Optional<City> partialUpdate(City city);

    /**
     * Get all the cities.
     *
     * @return the list of entities.
     */
    List<City> findAll();

    /**
     * Get all the City where UserAttributes is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<City> findAllWhereUserAttributesIsNull();

    /**
     * Get all the cities with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<City> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" city.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<City> findOne(Long id);

    /**
     * Delete the "id" city.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
