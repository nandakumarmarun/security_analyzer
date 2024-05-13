package com.secitriy.analyzer.repository;

import com.secitriy.analyzer.domain.UserAttributes;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserAttributes entity.
 */
@Repository
public interface UserAttributesRepository extends JpaRepository<UserAttributes, Long> {
    default Optional<UserAttributes> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<UserAttributes> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<UserAttributes> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select userAttributes from UserAttributes userAttributes left join fetch userAttributes.country left join fetch userAttributes.state left join fetch userAttributes.district left join fetch userAttributes.city left join fetch userAttributes.location",
        countQuery = "select count(userAttributes) from UserAttributes userAttributes"
    )
    Page<UserAttributes> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select userAttributes from UserAttributes userAttributes left join fetch userAttributes.country left join fetch userAttributes.state left join fetch userAttributes.district left join fetch userAttributes.city left join fetch userAttributes.location"
    )
    List<UserAttributes> findAllWithToOneRelationships();

    @Query(
        "select userAttributes from UserAttributes userAttributes left join fetch userAttributes.country left join fetch userAttributes.state left join fetch userAttributes.district left join fetch userAttributes.city left join fetch userAttributes.location where userAttributes.id =:id"
    )
    Optional<UserAttributes> findOneWithToOneRelationships(@Param("id") Long id);
}
