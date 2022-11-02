package com.booking.tai.web.rest;

import com.booking.tai.domain.Restaurant;
import com.booking.tai.domain.Services;
import com.booking.tai.domain.User;
import com.booking.tai.repository.RestaurantRepository;
import com.booking.tai.repository.ServicesRepository;
import com.booking.tai.service.ServicesService;
import com.booking.tai.service.UserService;
import com.booking.tai.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.booking.tai.domain.Services}.
 */
@RestController
@RequestMapping("/api")
public class ServicesResource {

    private final Logger log = LoggerFactory.getLogger(ServicesResource.class);

    private static final String ENTITY_NAME = "services";

    private final UserService userService;

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServicesService servicesService;

    private final ServicesRepository servicesRepository;

    private final RestaurantRepository restaurantRepository;

    public ServicesResource(UserService userService, ServicesService servicesService, ServicesRepository servicesRepository, RestaurantRepository restaurantRepository) {
        this.userService = userService;
        this.servicesService = servicesService;
        this.servicesRepository = servicesRepository;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * {@code POST  /services} : Create a new services.
     *
     * @param services the services to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new services, or with status {@code 400 (Bad Request)} if the services has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/manager/regis-services")
    public ResponseEntity<Services> createServices(@Valid @RequestBody Services services) throws URISyntaxException {
        log.debug("REST request to save Services : {}", services);
        if (services.getId() != null) {
            throw new BadRequestAlertException("A new services cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User manager = userService.getUserWithAuthorities().get();
        Restaurant restaurant = restaurantRepository.findById(services.getRestaurant().getId()).get();
        if(restaurant.getActive() == 0){
            throw new BadRequestAlertException("This restaurant not active", ENTITY_NAME, "active");
        }
        if(restaurantRepository.checkById(restaurant.getId(), manager.getId()).isEmpty()){
            throw new BadRequestAlertException("access dinied", ENTITY_NAME, "401");
        }
        Services result = servicesService.save(services);
        return ResponseEntity
            .created(new URI("/api/services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/manager/update-services/{id}")
    public ResponseEntity<Services> updateServices(@Valid @RequestBody Services services, @PathVariable(value = "id") Long id) throws URISyntaxException {
        log.debug("REST request to save Services : {}", services);
        if (services.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, services.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        User manager = userService.getUserWithAuthorities().get();
        Services ser = servicesRepository.findById(id).get();
        if(restaurantRepository.checkById(ser.getRestaurant().getId(), manager.getId()).isEmpty()){
            throw new BadRequestAlertException("access dinied", ENTITY_NAME, "401");
        }
        services.setRestaurant(ser.getRestaurant());

        Services result = servicesService.save(services);
        return ResponseEntity
            .created(new URI("/api/services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @DeleteMapping("/manager/delete-services/{id}")
    public  ResponseEntity<Void> delete(@PathVariable(value = "id") Long id){
        User manager = userService.getUserWithAuthorities().get();
        Services services = servicesRepository.findById(id).get();
        if(restaurantRepository.checkById(services.getRestaurant().getId(), manager.getId()).isEmpty()){
            throw new BadRequestAlertException("access dinied", ENTITY_NAME, "401");
        }
        servicesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }


    /**
     * {@code PUT  /services/:id} : Updates an existing services.
     *
     * @param id the id of the services to save.
     * @param services the services to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated services,
     * or with status {@code 400 (Bad Request)} if the services is not valid,
     * or with status {@code 500 (Internal Server Error)} if the services couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/services/{id}")
    public ResponseEntity<Services> updateServices(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Services services
    ) throws URISyntaxException {
        log.debug("REST request to update Services : {}, {}", id, services);
        if (services.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, services.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Services result = servicesService.save(services);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, services.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /services/:id} : Partial updates given fields of an existing services, field will ignore if it is null
     *
     * @param id the id of the services to save.
     * @param services the services to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated services,
     * or with status {@code 400 (Bad Request)} if the services is not valid,
     * or with status {@code 404 (Not Found)} if the services is not found,
     * or with status {@code 500 (Internal Server Error)} if the services couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/services/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Services> partialUpdateServices(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Services services
    ) throws URISyntaxException {
        log.debug("REST request to partial update Services partially : {}, {}", id, services);
        if (services.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, services.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!servicesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Services> result = servicesService.partialUpdate(services);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, services.getId().toString())
        );
    }

    /**
     * {@code GET  /services} : get all the services.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of services in body.
     */
    @GetMapping("/services")
    public List<Services> getAllServices() {
        log.debug("REST request to get all Services");
        return servicesService.findAll();
    }

    @GetMapping("/public/search-service")
    public Page<Services> search(@RequestParam(value = "query") String param, Pageable pageable){
        return servicesRepository.search("%"+param+"%", pageable);
    }

    @GetMapping("/public/service-by-restaurant/{id}")
    public Page<Services> findByRestaurant(@PathVariable(value = "id") Long id, Pageable pageable){
        return servicesRepository.findByRestaurant(id, pageable);
    }

    @GetMapping("/public/services/{id}")
    public Services findById(@PathVariable(value = "id") Long id ){
        return servicesService.findOne(id).get();
    }

    /**
     * {@code GET  /services/:id} : get the "id" services.
     *
     * @param id the id of the services to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the services, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/services/{id}")
    public ResponseEntity<Services> getServices(@PathVariable Long id) {
        log.debug("REST request to get Services : {}", id);
        Optional<Services> services = servicesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(services);
    }

    /**
     * {@code DELETE  /services/:id} : delete the "id" services.
     *
     * @param id the id of the services to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/services/{id}")
    public ResponseEntity<Void> deleteServices(@PathVariable Long id) {
        log.debug("REST request to delete Services : {}", id);
        servicesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
