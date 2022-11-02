package com.booking.tai.web.rest;

import com.booking.tai.domain.ImageRoom;
import com.booking.tai.repository.ImageRoomRepository;
import com.booking.tai.service.ImageRoomService;
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
 * REST controller for managing {@link com.booking.tai.domain.ImageRoom}.
 */
@RestController
@RequestMapping("/api")
public class ImageRoomResource {

    private final Logger log = LoggerFactory.getLogger(ImageRoomResource.class);

    private static final String ENTITY_NAME = "imageRoom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageRoomService imageRoomService;

    private final ImageRoomRepository imageRoomRepository;

    public ImageRoomResource(ImageRoomService imageRoomService, ImageRoomRepository imageRoomRepository) {
        this.imageRoomService = imageRoomService;
        this.imageRoomRepository = imageRoomRepository;
    }

    /**
     * {@code POST  /image-rooms} : Create a new imageRoom.
     *
     * @param imageRoom the imageRoom to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imageRoom, or with status {@code 400 (Bad Request)} if the imageRoom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/image-rooms")
    public ResponseEntity<ImageRoom> createImageRoom(@RequestBody ImageRoom imageRoom) throws URISyntaxException {
        log.debug("REST request to save ImageRoom : {}", imageRoom);
        if (imageRoom.getId() != null) {
            throw new BadRequestAlertException("A new imageRoom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ImageRoom result = imageRoomService.save(imageRoom);
        return ResponseEntity
            .created(new URI("/api/image-rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /image-rooms/:id} : Updates an existing imageRoom.
     *
     * @param id the id of the imageRoom to save.
     * @param imageRoom the imageRoom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageRoom,
     * or with status {@code 400 (Bad Request)} if the imageRoom is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imageRoom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/image-rooms/{id}")
    public ResponseEntity<ImageRoom> updateImageRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImageRoom imageRoom
    ) throws URISyntaxException {
        log.debug("REST request to update ImageRoom : {}, {}", id, imageRoom);
        if (imageRoom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageRoom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ImageRoom result = imageRoomService.save(imageRoom);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, imageRoom.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /image-rooms/:id} : Partial updates given fields of an existing imageRoom, field will ignore if it is null
     *
     * @param id the id of the imageRoom to save.
     * @param imageRoom the imageRoom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageRoom,
     * or with status {@code 400 (Bad Request)} if the imageRoom is not valid,
     * or with status {@code 404 (Not Found)} if the imageRoom is not found,
     * or with status {@code 500 (Internal Server Error)} if the imageRoom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/image-rooms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImageRoom> partialUpdateImageRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImageRoom imageRoom
    ) throws URISyntaxException {
        log.debug("REST request to partial update ImageRoom partially : {}, {}", id, imageRoom);
        if (imageRoom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageRoom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImageRoom> result = imageRoomService.partialUpdate(imageRoom);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, imageRoom.getId().toString())
        );
    }

    /**
     * {@code GET  /image-rooms} : get all the imageRooms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imageRooms in body.
     */
    @GetMapping("/image-rooms")
    public List<ImageRoom> getAllImageRooms() {
        log.debug("REST request to get all ImageRooms");
        return imageRoomService.findAll();
    }

    /**
     * {@code GET  /image-rooms/:id} : get the "id" imageRoom.
     *
     * @param id the id of the imageRoom to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imageRoom, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/image-rooms/{id}")
    public ResponseEntity<ImageRoom> getImageRoom(@PathVariable Long id) {
        log.debug("REST request to get ImageRoom : {}", id);
        Optional<ImageRoom> imageRoom = imageRoomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imageRoom);
    }

    /**
     * {@code DELETE  /image-rooms/:id} : delete the "id" imageRoom.
     *
     * @param id the id of the imageRoom to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/image-rooms/{id}")
    public ResponseEntity<Void> deleteImageRoom(@PathVariable Long id) {
        log.debug("REST request to delete ImageRoom : {}", id);
        imageRoomService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
