package com.secitriy.analyzer.service;

import com.secitriy.analyzer.domain.CheckLisItem;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.secitriy.analyzer.domain.CheckLisItem}.
 */
public interface CheckLisItemService {
    /**
     * Save a checkLisItem.
     *
     * @param checkLisItem the entity to save.
     * @return the persisted entity.
     */
    CheckLisItem save(CheckLisItem checkLisItem);

    /**
     * Updates a checkLisItem.
     *
     * @param checkLisItem the entity to update.
     * @return the persisted entity.
     */
    CheckLisItem update(CheckLisItem checkLisItem);

    /**
     * Partially updates a checkLisItem.
     *
     * @param checkLisItem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CheckLisItem> partialUpdate(CheckLisItem checkLisItem);

    /**
     * Get all the checkLisItems.
     *
     * @return the list of entities.
     */
    List<CheckLisItem> findAll();

    /**
     * Get all the CheckLisItem where TestCheckLisItem is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<CheckLisItem> findAllWhereTestCheckLisItemIsNull();

    /**
     * Get the "id" checkLisItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CheckLisItem> findOne(Long id);

    /**
     * Delete the "id" checkLisItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
