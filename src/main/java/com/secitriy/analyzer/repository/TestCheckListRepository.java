package com.secitriy.analyzer.repository;

import com.secitriy.analyzer.domain.TestCheckList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCheckList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TestCheckListRepository extends JpaRepository<TestCheckList, Long> {}
