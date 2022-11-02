package com.booking.tai.repository;

import com.booking.tai.domain.ImageRoom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ImageRoom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageRoomRepository extends JpaRepository<ImageRoom, Long> {}
