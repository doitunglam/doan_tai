package com.booking.tai.repository;

import com.booking.tai.domain.Hotel;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface HotelRepositoryWithBagRelationships {
    Optional<Hotel> fetchBagRelationships(Optional<Hotel> hotel);

    List<Hotel> fetchBagRelationships(List<Hotel> hotels);

    Page<Hotel> fetchBagRelationships(Page<Hotel> hotels);
}
