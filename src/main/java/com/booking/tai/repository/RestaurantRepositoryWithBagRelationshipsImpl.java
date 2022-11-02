package com.booking.tai.repository;

import com.booking.tai.domain.Restaurant;
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
public class RestaurantRepositoryWithBagRelationshipsImpl implements RestaurantRepositoryWithBagRelationships {

    @Autowired
    private EntityManager entityManager;

    @Override
    public Optional<Restaurant> fetchBagRelationships(Optional<Restaurant> restaurant) {
        return restaurant.map(this::fetchUsers);
    }

    @Override
    public Page<Restaurant> fetchBagRelationships(Page<Restaurant> restaurants) {
        return new PageImpl<>(fetchBagRelationships(restaurants.getContent()), restaurants.getPageable(), restaurants.getTotalElements());
    }

    @Override
    public List<Restaurant> fetchBagRelationships(List<Restaurant> restaurants) {
        return Optional.of(restaurants).map(this::fetchUsers).get();
    }

    Restaurant fetchUsers(Restaurant result) {
        return entityManager
            .createQuery(
                "select restaurant from Restaurant restaurant left join fetch restaurant.users where restaurant is :restaurant",
                Restaurant.class
            )
            .setParameter("restaurant", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Restaurant> fetchUsers(List<Restaurant> restaurants) {
        return entityManager
            .createQuery(
                "select distinct restaurant from Restaurant restaurant left join fetch restaurant.users where restaurant in :restaurants",
                Restaurant.class
            )
            .setParameter("restaurants", restaurants)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
