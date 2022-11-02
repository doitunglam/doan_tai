package com.booking.tai.web.rest;

import com.booking.tai.domain.ImageServices;
import com.booking.tai.repository.ImageServicesRepository;
import com.booking.tai.service.ImageServicesService;
import com.booking.tai.service.UserService;
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
 * REST controller for managing {@link com.booking.tai.domain.ImageServices}.
 */
@RestController
@RequestMapping("/api")
public class ImageServicesResource {

    private final Logger log = LoggerFactory.getLogger(ImageServicesResource.class);

    private static final String ENTITY_NAME = "imageServices";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageServicesService imageServicesService;

    private final ImageServicesRepository imageServicesRepository;

    private final UserService userService;

    public ImageServicesResource(ImageServicesService imageServicesService, ImageServicesRepository imageServicesRepository, UserService userService) {
        this.imageServicesService = imageServicesService;
        this.imageServicesRepository = imageServicesRepository;
        this.userService = userService;
    }

    /**
     * {@code POST  /image-services} : Create a new imageServices.
     *
     * @param imageServices the imageServices to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imageServices, or with status {@code 400 (Bad Request)} if the imageServices has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/manager/image-services")
    public ResponseEntity<ImageServices> createImageServices(@RequestBody ImageServices imageServices) throws URISyntaxException {
        log.debug("REST request to save ImageServices : {}", imageServices);
        if (imageServices.getId() != null) {
            throw new BadRequestAlertException("A new imageServices cannot already have an ID", ENTITY_NAME, "idexists");
        }

        ImageServices result = imageServicesService.save(imageServices);
        return ResponseEntity
            .created(new URI("/api/image-services/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /image-services/:id} : Updates an existing imageServices.
     *
     * @param id the id of the imageServices to save.
     * @param imageServices the imageServices to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageServices,
     * or with status {@code 400 (Bad Request)} if the imageServices is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imageServices couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/image-services/{id}")
    public ResponseEntity<ImageServices> updateImageServices(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImageServices imageServices
    ) throws URISyntaxException {
        log.debug("REST request to update ImageServices : {}, {}", id, imageServices);
        if (imageServices.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageServices.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageServicesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ImageServices result = imageServicesService.save(imageServices);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, imageServices.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /image-services/:id} : Partial updates given fields of an existing imageServices, field will ignore if it is null
     *
     * @param id the id of the imageServices to save.
     * @param imageServices the imageServices to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageServices,
     * or with status {@code 400 (Bad Request)} if the imageServices is not valid,
     * or with status {@code 404 (Not Found)} if the imageServices is not found,
     * or with status {@code 500 (Internal Server Error)} if the imageServices couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/image-services/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImageServices> partialUpdateImageServices(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ImageServices imageServices
    ) throws URISyntaxException {
        log.debug("REST request to partial update ImageServices partially : {}, {}", id, imageServices);
        if (imageServices.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageServices.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageServicesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImageServices> result = imageServicesService.partialUpdate(imageServices);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, imageServices.getId().toString())
        );
    }

    /**
     * {@code GET  /image-services} : get all the imageServices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imageServices in body.
     */
    @GetMapping("/image-services")
    public List<ImageServices> getAllImageServices() {
        log.debug("REST request to get all ImageServices");
        return imageServicesService.findAll();
    }

    /**
     * {@code GET  /image-services/:id} : get the "id" imageServices.
     *
     * @param id the id of the imageServices to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imageServices, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/image-services/{id}")
    public ResponseEntity<ImageServices> getImageServices(@PathVariable Long id) {
        log.debug("REST request to get ImageServices : {}", id);
        Optional<ImageServices> imageServices = imageServicesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imageServices);
    }

    /**
     * {@code DELETE  /image-services/:id} : delete the "id" imageServices.
     *
     * @param id the id of the imageServices to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/image-services/{id}")
    public ResponseEntity<Void> deleteImageServices(@PathVariable Long id) {
        log.debug("REST request to delete ImageServices : {}", id);
        imageServicesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
