package com.secitriy.analyzer.service;

import com.secitriy.analyzer.domain.TestCheckList;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.secitriy.analyzer.domain.TestCheckList}.
 */
public interface TestCheckListService {
    /**
     * Save a testCheckList.
     *
     * @param testCheckList the entity to save.
     * @return the persisted entity.
     */
    TestCheckList save(TestCheckList testCheckList);

    /**
     * Updates a testCheckList.
     *
     * @param testCheckList the entity to update.
     * @return the persisted entity.
     */
    TestCheckList update(TestCheckList testCheckList);

    /**
     * Partially updates a testCheckList.
     *
     * @param testCheckList the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TestCheckList> partialUpdate(TestCheckList testCheckList);

    /**
     * Get all the testCheckLists.
     *
     * @return the list of entities.
     */
    List<TestCheckList> findAll();

    /**
     * Get the "id" testCheckList.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TestCheckList> findOne(Long id);

    /**
     * Delete the "id" testCheckList.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
