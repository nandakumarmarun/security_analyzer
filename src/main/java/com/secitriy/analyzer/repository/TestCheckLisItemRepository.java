package com.secitriy.analyzer.repository;

import com.secitriy.analyzer.domain.TestCheckLisItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TestCheckLisItem entity.
 */
@Repository
public interface TestCheckLisItemRepository extends JpaRepository<TestCheckLisItem, Long> {
    default Optional<TestCheckLisItem> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TestCheckLisItem> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TestCheckLisItem> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select testCheckLisItem from TestCheckLisItem testCheckLisItem left join fetch testCheckLisItem.checklistitem",
        countQuery = "select count(testCheckLisItem) from TestCheckLisItem testCheckLisItem"
    )
    Page<TestCheckLisItem> findAllWithToOneRelationships(Pageable pageable);

    @Query("select testCheckLisItem from TestCheckLisItem testCheckLisItem left join fetch testCheckLisItem.checklistitem")
    List<TestCheckLisItem> findAllWithToOneRelationships();

    @Query(
        "select testCheckLisItem from TestCheckLisItem testCheckLisItem left join fetch testCheckLisItem.checklistitem where testCheckLisItem.id =:id"
    )
    Optional<TestCheckLisItem> findOneWithToOneRelationships(@Param("id") Long id);
}
