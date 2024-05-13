package com.secitriy.analyzer.repository;

import com.secitriy.analyzer.domain.SecurityTest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SecurityTest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SecurityTestRepository extends JpaRepository<SecurityTest, Long> {}
