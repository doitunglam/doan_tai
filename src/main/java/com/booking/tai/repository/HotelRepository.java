package com.booking.tai.repository;

import com.booking.tai.domain.Hotel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Hotel entity.
 */
@Repository
public interface HotelRepository extends HotelRepositoryWithBagRelationships, JpaRepository<Hotel, Long> {
    default Optional<Hotel> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Hotel> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Hotel> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    @Query(value = "select h.* from hotel h inner join rel_hotel__user u on u.hotel_id = h.id where u.hotel_id =?1 and u.user_id = ?2",nativeQuery = true)
    Optional<Hotel> findByUserAndHotelId(Long hotelId, Long UserId);

    @Query(value = "select h.* from hotel h inner join rel_hotel__user u on u.hotel_id = h.id where u.user_id = ?1", nativeQuery = true)
    Page<Hotel> findByUser(Long userId, Pageable pageable);
}
