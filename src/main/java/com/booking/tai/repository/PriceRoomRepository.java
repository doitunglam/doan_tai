package com.booking.tai.repository;

import com.booking.tai.domain.PriceRoom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PriceRoom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PriceRoomRepository extends JpaRepository<PriceRoom, Long> {}
