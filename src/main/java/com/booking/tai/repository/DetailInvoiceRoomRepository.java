package com.booking.tai.repository;

import com.booking.tai.domain.DetailInvoiceRoom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DetailInvoiceRoom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetailInvoiceRoomRepository extends JpaRepository<DetailInvoiceRoom, Long> {}
