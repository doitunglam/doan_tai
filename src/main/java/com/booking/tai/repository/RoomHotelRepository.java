package com.booking.tai.repository;

import com.booking.tai.domain.RoomHotel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the RoomHotel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RoomHotelRepository extends JpaRepository<RoomHotel, Long> {}
