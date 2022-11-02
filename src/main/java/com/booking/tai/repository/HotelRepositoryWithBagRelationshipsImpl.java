package com.booking.tai.repository;

import com.booking.tai.domain.Hotel;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.hibernate.annotations.QueryHints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class HotelRepositoryWithBagRelationshipsImpl implements HotelRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Hotel> fetchBagRelationships(Optional<Hotel> hotel) {
        return hotel.map(this::fetchUsers);
    }

    @Override
    public Page<Hotel> fetchBagRelationships(Page<Hotel> hotels) {
        return new PageImpl<>(fetchBagRelationships(hotels.getContent()), hotels.getPageable(), hotels.getTotalElements());
    }

    @Override
    public List<Hotel> fetchBagRelationships(List<Hotel> hotels) {
        return Optional.of(hotels).map(this::fetchUsers).get();
    }

    Hotel fetchUsers(Hotel result) {
        return entityManager
            .createQuery("select hotel from Hotel hotel left join fetch hotel.users where hotel is :hotel", Hotel.class)
            .setParameter("hotel", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Hotel> fetchUsers(List<Hotel> hotels) {
        return entityManager
            .createQuery("select distinct hotel from Hotel hotel left join fetch hotel.users where hotel in :hotels", Hotel.class)
            .setParameter("hotels", hotels)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
