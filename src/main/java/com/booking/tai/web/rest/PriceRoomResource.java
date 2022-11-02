package com.booking.tai.web.rest;

import com.booking.tai.domain.PriceRoom;
import com.booking.tai.repository.PriceRoomRepository;
import com.booking.tai.service.PriceRoomService;
import com.booking.tai.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.booking.tai.domain.PriceRoom}.
 */
@RestController
@RequestMapping("/api")
public class PriceRoomResource {

    private final Logger log = LoggerFactory.getLogger(PriceRoomResource.class);

    private static final String ENTITY_NAME = "priceRoom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PriceRoomService priceRoomService;

    private final PriceRoomRepository priceRoomRepository;

    public PriceRoomResource(PriceRoomService priceRoomService, PriceRoomRepository priceRoomRepository) {
        this.priceRoomService = priceRoomService;
        this.priceRoomRepository = priceRoomRepository;
    }

    /**
     * {@code POST  /price-rooms} : Create a new priceRoom.
     *
     * @param priceRoom the priceRoom to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new priceRoom, or with status {@code 400 (Bad Request)} if the priceRoom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/manager/price-rooms")
    public ResponseEntity<PriceRoom> createPriceRoom(@RequestBody PriceRoom priceRoom) throws URISyntaxException {
        log.debug("REST request to save PriceRoom : {}", priceRoom);
        if (priceRoom.getId() != null) {
            throw new BadRequestAlertException("A new priceRoom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PriceRoom result = priceRoomService.save(priceRoom);
        return ResponseEntity
            .created(new URI("/api/price-rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /price-rooms/:id} : Updates an existing priceRoom.
     *
     * @param id the id of the priceRoom to save.
     * @param priceRoom the priceRoom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priceRoom,
     * or with status {@code 400 (Bad Request)} if the priceRoom is not valid,
     * or with status {@code 500 (Internal Server Error)} if the priceRoom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/price-rooms/{id}")
    public ResponseEntity<PriceRoom> updatePriceRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PriceRoom priceRoom
    ) throws URISyntaxException {
        log.debug("REST request to update PriceRoom : {}, {}", id, priceRoom);
        if (priceRoom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceRoom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PriceRoom result = priceRoomService.save(priceRoom);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, priceRoom.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /price-rooms/:id} : Partial updates given fields of an existing priceRoom, field will ignore if it is null
     *
     * @param id the id of the priceRoom to save.
     * @param priceRoom the priceRoom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated priceRoom,
     * or with status {@code 400 (Bad Request)} if the priceRoom is not valid,
     * or with status {@code 404 (Not Found)} if the priceRoom is not found,
     * or with status {@code 500 (Internal Server Error)} if the priceRoom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/price-rooms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PriceRoom> partialUpdatePriceRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PriceRoom priceRoom
    ) throws URISyntaxException {
        log.debug("REST request to partial update PriceRoom partially : {}, {}", id, priceRoom);
        if (priceRoom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, priceRoom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!priceRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PriceRoom> result = priceRoomService.partialUpdate(priceRoom);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, priceRoom.getId().toString())
        );
    }

    /**
     * {@code GET  /price-rooms} : get all the priceRooms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of priceRooms in body.
     */
    @GetMapping("/price-rooms")
    public List<PriceRoom> getAllPriceRooms() {
        log.debug("REST request to get all PriceRooms");
        return priceRoomService.findAll();
    }

    /**
     * {@code GET  /price-rooms/:id} : get the "id" priceRoom.
     *
     * @param id the id of the priceRoom to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the priceRoom, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/price-rooms/{id}")
    public ResponseEntity<PriceRoom> getPriceRoom(@PathVariable Long id) {
        log.debug("REST request to get PriceRoom : {}", id);
        Optional<PriceRoom> priceRoom = priceRoomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(priceRoom);
    }

    /**
     * {@code DELETE  /price-rooms/:id} : delete the "id" priceRoom.
     *
     * @param id the id of the priceRoom to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/price-rooms/{id}")
    public ResponseEntity<Void> deletePriceRoom(@PathVariable Long id) {
        log.debug("REST request to delete PriceRoom : {}", id);
        priceRoomService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
