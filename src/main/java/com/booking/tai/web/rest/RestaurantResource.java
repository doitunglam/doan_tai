package com.booking.tai.web.rest;

import com.booking.tai.domain.Authority;
import com.booking.tai.domain.Restaurant;
import com.booking.tai.domain.User;
import com.booking.tai.repository.RestaurantRepository;
import com.booking.tai.service.RestaurantService;
import com.booking.tai.service.UserService;
import com.booking.tai.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.booking.tai.domain.Restaurant}.
 */
@RestController
@RequestMapping("/api")
public class RestaurantResource {

    private final Logger log = LoggerFactory.getLogger(RestaurantResource.class);

    private static final String ENTITY_NAME = "restaurant";

    private final UserService userService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RestaurantService restaurantService;

    private final RestaurantRepository restaurantRepository;

    public RestaurantResource(UserService userService, RestaurantService restaurantService, RestaurantRepository restaurantRepository) {
        this.userService = userService;
        this.restaurantService = restaurantService;
        this.restaurantRepository = restaurantRepository;
    }


    @PostMapping("/manager/restaurants")
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody Restaurant restaurant) throws URISyntaxException, AuthenticationException {
        log.debug("REST request to save Restaurant : {}", restaurant);
        if (restaurant.getId() != null) {
            throw new BadRequestAlertException("A new restaurant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User user = userService.getUserWithAuthorities().get();
        restaurant.getUsers().add(user);
        restaurant.setActive(0);
        UUID uuid = UUID.randomUUID();
        restaurant.setToken(uuid.toString());
        Restaurant result = restaurantService.save(restaurant);
        return ResponseEntity
            .created(new URI("/api/restaurants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }



    @GetMapping("/restaurants")
    public List<Restaurant> getAllRestaurants(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Restaurants");
        return restaurantService.findAll();
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<Restaurant> getRestaurant(@PathVariable Long id) {
        log.debug("REST request to get Restaurant : {}", id);
        Optional<Restaurant> restaurant = restaurantService.findOne(id);
        return ResponseUtil.wrapOrNotFound(restaurant);
    }

    // lay ra danh sach nha hang cua user dang dang nhap
    @GetMapping("/list-my-restaurants")
    public Page<Restaurant> getListRestaurantByUser(Pageable pageable) {
        User user = userService.getUserWithAuthorities().get();
        return restaurantRepository.findByUser(user.getId(),pageable);
    }

    /**
     * {@code DELETE  /restaurants/:id} : delete the "id" restaurant.
     *
     * @param id the id of the restaurant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/restaurants/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        log.debug("REST request to delete Restaurant : {}", id);
        restaurantService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    @PostMapping("/admin/confirm-restaurant/{id}")
    public Restaurant createRestaurant(@PathVariable Long id){
        Restaurant restaurant = restaurantRepository.findById(id).get();
        restaurant.setActive(1);
        restaurantRepository.save(restaurant);
        return restaurant;
    }

    @PostMapping("/manager/addemployee-restaurant/{id}")
    public Restaurant addEmployeeToRestaurant(@PathVariable Long id, @RequestBody User user){
        User manager = userService.getUserWithAuthorities().get();
        Restaurant restaurant = restaurantRepository.findById(id).get();
        if(restaurantRepository.checkById(restaurant.getId(), manager.getId()).get() == null){
            throw new BadRequestAlertException("not found your account in this restaurant","Restaurant","401");
        }
        restaurant.getUsers().add(user);
        restaurantRepository.save(restaurant);
        return restaurant;
    }
}
