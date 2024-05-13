package com.secitriy.analyzer.service;

import com.secitriy.analyzer.domain.State;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.secitriy.analyzer.domain.State}.
 */
public interface StateService {
    /**
     * Save a state.
     *
     * @param state the entity to save.
     * @return the persisted entity.
     */
    State save(State state);

    /**
     * Updates a state.
     *
     * @param state the entity to update.
     * @return the persisted entity.
     */
    State update(State state);

    /**
     * Partially updates a state.
     *
     * @param state the entity to update partially.
     * @return the persisted entity.
     */
    Optional<State> partialUpdate(State state);

    /**
     * Get all the states.
     *
     * @return the list of entities.
     */
    List<State> findAll();

    /**
     * Get all the State where UserAttributes is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<State> findAllWhereUserAttributesIsNull();

    /**
     * Get all the states with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<State> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" state.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<State> findOne(Long id);

    /**
     * Delete the "id" state.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
