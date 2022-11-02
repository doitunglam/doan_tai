package com.booking.tai.repository;

import com.booking.tai.domain.Services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data SQL repository for the Services entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ServicesRepository extends JpaRepository<Services, Long> {

    @Query(value = "select s.* from services s where s.id = ?1", nativeQuery = true)
    Optional<Services> findById(Long id);

    @Query(value = "select s.* from services s where s.service_name like ?1 or s.address like ?1 or s.description like ?1", nativeQuery = true)
    Page<Services> search(String param, Pageable pageable);

    @Query(value = "select s.* from services s where s.restaurant_id=?1", nativeQuery = true)
    Page<Services> findByRestaurant(Long id, Pageable pageable);
}
