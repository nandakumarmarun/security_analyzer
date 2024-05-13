package com.secitriy.analyzer.repository;

import com.secitriy.analyzer.domain.CheckLisItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CheckLisItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckLisItemRepository extends JpaRepository<CheckLisItem, Long> {}
