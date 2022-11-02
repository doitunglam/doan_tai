package com.booking.tai.repository;

import com.booking.tai.domain.ImageServices;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ImageServices entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ImageServicesRepository extends JpaRepository<ImageServices, Long> {}
