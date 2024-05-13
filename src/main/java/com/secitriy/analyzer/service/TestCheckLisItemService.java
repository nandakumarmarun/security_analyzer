package com.secitriy.analyzer.service;

import com.secitriy.analyzer.domain.TestCheckLisItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.secitriy.analyzer.domain.TestCheckLisItem}.
 */
public interface TestCheckLisItemService {
    /**
     * Save a testCheckLisItem.
     *
     * @param testCheckLisItem the entity to save.
     * @return the persisted entity.
     */
    TestCheckLisItem save(TestCheckLisItem testCheckLisItem);

    /**
     * Updates a testCheckLisItem.
     *
     * @param testCheckLisItem the entity to update.
     * @return the persisted entity.
     */
    TestCheckLisItem update(TestCheckLisItem testCheckLisItem);

    /**
     * Partially updates a testCheckLisItem.
     *
     * @param testCheckLisItem the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TestCheckLisItem> partialUpdate(TestCheckLisItem testCheckLisItem);

    /**
     * Get all the testCheckLisItems.
     *
     * @return the list of entities.
     */
    List<TestCheckLisItem> findAll();

    /**
     * Get all the testCheckLisItems with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TestCheckLisItem> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" testCheckLisItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TestCheckLisItem> findOne(Long id);

    /**
     * Delete the "id" testCheckLisItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
