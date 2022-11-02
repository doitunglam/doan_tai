package com.booking.tai.web.rest;

import com.booking.tai.domain.DetailInvoiceRoom;
import com.booking.tai.repository.DetailInvoiceRoomRepository;
import com.booking.tai.service.DetailInvoiceRoomService;
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
 * REST controller for managing {@link com.booking.tai.domain.DetailInvoiceRoom}.
 */
@RestController
@RequestMapping("/api")
public class DetailInvoiceRoomResource {

    private final Logger log = LoggerFactory.getLogger(DetailInvoiceRoomResource.class);

    private static final String ENTITY_NAME = "detailInvoiceRoom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetailInvoiceRoomService detailInvoiceRoomService;

    private final DetailInvoiceRoomRepository detailInvoiceRoomRepository;

    public DetailInvoiceRoomResource(
        DetailInvoiceRoomService detailInvoiceRoomService,
        DetailInvoiceRoomRepository detailInvoiceRoomRepository
    ) {
        this.detailInvoiceRoomService = detailInvoiceRoomService;
        this.detailInvoiceRoomRepository = detailInvoiceRoomRepository;
    }

    /**
     * {@code POST  /detail-invoice-rooms} : Create a new detailInvoiceRoom.
     *
     * @param detailInvoiceRoom the detailInvoiceRoom to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detailInvoiceRoom, or with status {@code 400 (Bad Request)} if the detailInvoiceRoom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/detail-invoice-rooms")
    public ResponseEntity<DetailInvoiceRoom> createDetailInvoiceRoom(@RequestBody DetailInvoiceRoom detailInvoiceRoom)
        throws URISyntaxException {
        log.debug("REST request to save DetailInvoiceRoom : {}", detailInvoiceRoom);
        if (detailInvoiceRoom.getId() != null) {
            throw new BadRequestAlertException("A new detailInvoiceRoom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetailInvoiceRoom result = detailInvoiceRoomService.save(detailInvoiceRoom);
        return ResponseEntity
            .created(new URI("/api/detail-invoice-rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /detail-invoice-rooms/:id} : Updates an existing detailInvoiceRoom.
     *
     * @param id the id of the detailInvoiceRoom to save.
     * @param detailInvoiceRoom the detailInvoiceRoom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailInvoiceRoom,
     * or with status {@code 400 (Bad Request)} if the detailInvoiceRoom is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detailInvoiceRoom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detail-invoice-rooms/{id}")
    public ResponseEntity<DetailInvoiceRoom> updateDetailInvoiceRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetailInvoiceRoom detailInvoiceRoom
    ) throws URISyntaxException {
        log.debug("REST request to update DetailInvoiceRoom : {}, {}", id, detailInvoiceRoom);
        if (detailInvoiceRoom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailInvoiceRoom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailInvoiceRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DetailInvoiceRoom result = detailInvoiceRoomService.save(detailInvoiceRoom);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, detailInvoiceRoom.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /detail-invoice-rooms/:id} : Partial updates given fields of an existing detailInvoiceRoom, field will ignore if it is null
     *
     * @param id the id of the detailInvoiceRoom to save.
     * @param detailInvoiceRoom the detailInvoiceRoom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailInvoiceRoom,
     * or with status {@code 400 (Bad Request)} if the detailInvoiceRoom is not valid,
     * or with status {@code 404 (Not Found)} if the detailInvoiceRoom is not found,
     * or with status {@code 500 (Internal Server Error)} if the detailInvoiceRoom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/detail-invoice-rooms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DetailInvoiceRoom> partialUpdateDetailInvoiceRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetailInvoiceRoom detailInvoiceRoom
    ) throws URISyntaxException {
        log.debug("REST request to partial update DetailInvoiceRoom partially : {}, {}", id, detailInvoiceRoom);
        if (detailInvoiceRoom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailInvoiceRoom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailInvoiceRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetailInvoiceRoom> result = detailInvoiceRoomService.partialUpdate(detailInvoiceRoom);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, detailInvoiceRoom.getId().toString())
        );
    }

    /**
     * {@code GET  /detail-invoice-rooms} : get all the detailInvoiceRooms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detailInvoiceRooms in body.
     */
    @GetMapping("/detail-invoice-rooms")
    public List<DetailInvoiceRoom> getAllDetailInvoiceRooms() {
        log.debug("REST request to get all DetailInvoiceRooms");
        return detailInvoiceRoomService.findAll();
    }

    /**
     * {@code GET  /detail-invoice-rooms/:id} : get the "id" detailInvoiceRoom.
     *
     * @param id the id of the detailInvoiceRoom to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detailInvoiceRoom, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detail-invoice-rooms/{id}")
    public ResponseEntity<DetailInvoiceRoom> getDetailInvoiceRoom(@PathVariable Long id) {
        log.debug("REST request to get DetailInvoiceRoom : {}", id);
        Optional<DetailInvoiceRoom> detailInvoiceRoom = detailInvoiceRoomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detailInvoiceRoom);
    }

    /**
     * {@code DELETE  /detail-invoice-rooms/:id} : delete the "id" detailInvoiceRoom.
     *
     * @param id the id of the detailInvoiceRoom to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detail-invoice-rooms/{id}")
    public ResponseEntity<Void> deleteDetailInvoiceRoom(@PathVariable Long id) {
        log.debug("REST request to delete DetailInvoiceRoom : {}", id);
        detailInvoiceRoomService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
