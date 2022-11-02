package com.booking.tai.repository;

import com.booking.tai.domain.Restaurant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Restaurant entity.
 */
@Repository
public interface RestaurantRepository extends RestaurantRepositoryWithBagRelationships, JpaRepository<Restaurant, Long> {
    default Optional<Restaurant> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<Restaurant> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Restaurant> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }

    @Query(value = "select r.* from restaurant r inner join rel_restaurant__user u on u.restaurant_id = r.id where u.user_id = ?1",nativeQuery = true)
    Page<Restaurant> findByUser(Long userId,Pageable pageable);

    @Query(value = "select r.* from restaurant r inner join rel_restaurant__user u on u.restaurant_id = r.id where u.restaurant_id =?1 and u.user_id = ?2",nativeQuery = true)
    Restaurant findById(Long resId, Long UserId);

    @Query(value = "select r.* from restaurant r inner join rel_restaurant__user u on u.restaurant_id = r.id where u.restaurant_id =?1 and u.user_id = ?2",nativeQuery = true)
    Optional<Restaurant> checkById(Long resId, Long UserId);

    @Query(value = "select r.* from restaurant r where r.id = ?1",nativeQuery = true)
    Optional<Restaurant> findById(Long id);
}
