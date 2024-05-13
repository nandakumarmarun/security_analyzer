package com.secitriy.analyzer.repository;

import com.secitriy.analyzer.domain.CheckList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CheckList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckListRepository extends JpaRepository<CheckList, Long> {}
