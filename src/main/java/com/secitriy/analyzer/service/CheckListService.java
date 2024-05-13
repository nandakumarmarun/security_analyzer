package com.secitriy.analyzer.service;

import com.secitriy.analyzer.domain.CheckList;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.secitriy.analyzer.domain.CheckList}.
 */
public interface CheckListService {
    /**
     * Save a checkList.
     *
     * @param checkList the entity to save.
     * @return the persisted entity.
     */
    CheckList save(CheckList checkList);

    /**
     * Updates a checkList.
     *
     * @param checkList the entity to update.
     * @return the persisted entity.
     */
    CheckList update(CheckList checkList);

    /**
     * Partially updates a checkList.
     *
     * @param checkList the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CheckList> partialUpdate(CheckList checkList);

    /**
     * Get all the checkLists.
     *
     * @return the list of entities.
     */
    List<CheckList> findAll();

    /**
     * Get all the CheckList where TestCheckList is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<CheckList> findAllWhereTestCheckListIsNull();

    /**
     * Get the "id" checkList.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CheckList> findOne(Long id);

    /**
     * Delete the "id" checkList.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
