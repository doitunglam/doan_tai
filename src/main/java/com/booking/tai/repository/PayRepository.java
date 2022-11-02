package com.booking.tai.repository;

import com.booking.tai.domain.Pay;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pay entity.
 */
@Repository
public interface PayRepository extends JpaRepository<Pay, Long> {
    @Query("select pay from Pay pay where pay.user.login = ?#{principal.username}")
    List<Pay> findByUserIsCurrentUser();

    default Optional<Pay> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Pay> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Pay> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(value = "select distinct pay from Pay pay left join fetch pay.user", countQuery = "select count(distinct pay) from Pay pay")
    Page<Pay> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct pay from Pay pay left join fetch pay.user")
    List<Pay> findAllWithToOneRelationships();

    @Query("select pay from Pay pay left join fetch pay.user where pay.id =:id")
    Optional<Pay> findOneWithToOneRelationships(@Param("id") Long id);
}
