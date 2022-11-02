package com.booking.tai.repository;

import com.booking.tai.domain.DetailInvoice;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the DetailInvoice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DetailInvoiceRepository extends JpaRepository<DetailInvoice, Long> {}
