package com.secitriy.analyzer.service;

import com.secitriy.analyzer.domain.SecurityTest;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.secitriy.analyzer.domain.SecurityTest}.
 */
public interface SecurityTestService {
    /**
     * Save a securityTest.
     *
     * @param securityTest the entity to save.
     * @return the persisted entity.
     */
    SecurityTest save(SecurityTest securityTest);

    /**
     * Updates a securityTest.
     *
     * @param securityTest the entity to update.
     * @return the persisted entity.
     */
    SecurityTest update(SecurityTest securityTest);

    /**
     * Partially updates a securityTest.
     *
     * @param securityTest the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SecurityTest> partialUpdate(SecurityTest securityTest);

    /**
     * Get all the securityTests.
     *
     * @return the list of entities.
     */
    List<SecurityTest> findAll();

    /**
     * Get the "id" securityTest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SecurityTest> findOne(Long id);

    /**
     * Delete the "id" securityTest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
