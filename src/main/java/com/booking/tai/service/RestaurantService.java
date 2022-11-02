package com.booking.tai.service;

import com.booking.tai.domain.Restaurant;
import com.booking.tai.repository.RestaurantRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Restaurant}.
 */
@Service
@Transactional
public class RestaurantService {

    private final Logger log = LoggerFactory.getLogger(RestaurantService.class);

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Save a restaurant.
     *
     * @param restaurant the entity to save.
     * @return the persisted entity.
     */
    public Restaurant save(Restaurant restaurant) {
        log.debug("Request to save Restaurant : {}", restaurant);
        return restaurantRepository.save(restaurant);
    }

    /**
     * Partially update a restaurant.
     *
     * @param restaurant the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Restaurant> partialUpdate(Restaurant restaurant) {
        log.debug("Request to partially update Restaurant : {}", restaurant);

        return restaurantRepository
            .findById(restaurant.getId())
            .map(existingRestaurant -> {
                if (restaurant.getResName() != null) {
                    existingRestaurant.setResName(restaurant.getResName());
                }
                if (restaurant.getAddress() != null) {
                    existingRestaurant.setAddress(restaurant.getAddress());
                }
                if (restaurant.getPhone() != null) {
                    existingRestaurant.setPhone(restaurant.getPhone());
                }
                if (restaurant.getToken() != null) {
                    existingRestaurant.setToken(restaurant.getToken());
                }
                if (restaurant.getLinkweb() != null) {
                    existingRestaurant.setLinkweb(restaurant.getLinkweb());
                }

                return existingRestaurant;
            })
            .map(restaurantRepository::save);
    }

    /**
     * Get all the restaurants.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Restaurant> findAll() {
        log.debug("Request to get all Restaurants");
        return restaurantRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the restaurants with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Restaurant> findAllWithEagerRelationships(Pageable pageable) {
        return restaurantRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one restaurant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Restaurant> findOne(Long id) {
        log.debug("Request to get Restaurant : {}", id);
        return restaurantRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the restaurant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Restaurant : {}", id);
        restaurantRepository.deleteById(id);
    }
}
