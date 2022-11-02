package com.booking.tai.web.rest;

import com.booking.tai.domain.DetailInvoice;
import com.booking.tai.repository.DetailInvoiceRepository;
import com.booking.tai.service.DetailInvoiceService;
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
 * REST controller for managing {@link com.booking.tai.domain.DetailInvoice}.
 */
@RestController
@RequestMapping("/api")
public class DetailInvoiceResource {

    private final Logger log = LoggerFactory.getLogger(DetailInvoiceResource.class);

    private static final String ENTITY_NAME = "detailInvoice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DetailInvoiceService detailInvoiceService;

    private final DetailInvoiceRepository detailInvoiceRepository;

    public DetailInvoiceResource(DetailInvoiceService detailInvoiceService, DetailInvoiceRepository detailInvoiceRepository) {
        this.detailInvoiceService = detailInvoiceService;
        this.detailInvoiceRepository = detailInvoiceRepository;
    }

    /**
     * {@code POST  /detail-invoices} : Create a new detailInvoice.
     *
     * @param detailInvoice the detailInvoice to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new detailInvoice, or with status {@code 400 (Bad Request)} if the detailInvoice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/user/detail-invoices")
    public ResponseEntity<DetailInvoice> createDetailInvoice(@RequestBody DetailInvoice detailInvoice) throws URISyntaxException {
        log.debug("REST request to save DetailInvoice : {}", detailInvoice);
        if (detailInvoice.getId() != null) {
            throw new BadRequestAlertException("A new detailInvoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DetailInvoice result = detailInvoiceService.save(detailInvoice);
        return ResponseEntity
            .created(new URI("/api/detail-invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /detail-invoices/:id} : Updates an existing detailInvoice.
     *
     * @param id the id of the detailInvoice to save.
     * @param detailInvoice the detailInvoice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailInvoice,
     * or with status {@code 400 (Bad Request)} if the detailInvoice is not valid,
     * or with status {@code 500 (Internal Server Error)} if the detailInvoice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/detail-invoices/{id}")
    public ResponseEntity<DetailInvoice> updateDetailInvoice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetailInvoice detailInvoice
    ) throws URISyntaxException {
        log.debug("REST request to update DetailInvoice : {}, {}", id, detailInvoice);
        if (detailInvoice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailInvoice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailInvoiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DetailInvoice result = detailInvoiceService.save(detailInvoice);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, detailInvoice.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /detail-invoices/:id} : Partial updates given fields of an existing detailInvoice, field will ignore if it is null
     *
     * @param id the id of the detailInvoice to save.
     * @param detailInvoice the detailInvoice to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated detailInvoice,
     * or with status {@code 400 (Bad Request)} if the detailInvoice is not valid,
     * or with status {@code 404 (Not Found)} if the detailInvoice is not found,
     * or with status {@code 500 (Internal Server Error)} if the detailInvoice couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/detail-invoices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DetailInvoice> partialUpdateDetailInvoice(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DetailInvoice detailInvoice
    ) throws URISyntaxException {
        log.debug("REST request to partial update DetailInvoice partially : {}, {}", id, detailInvoice);
        if (detailInvoice.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, detailInvoice.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!detailInvoiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DetailInvoice> result = detailInvoiceService.partialUpdate(detailInvoice);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, detailInvoice.getId().toString())
        );
    }

    /**
     * {@code GET  /detail-invoices} : get all the detailInvoices.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of detailInvoices in body.
     */
    @GetMapping("/detail-invoices")
    public List<DetailInvoice> getAllDetailInvoices() {
        log.debug("REST request to get all DetailInvoices");
        return detailInvoiceService.findAll();
    }

    /**
     * {@code GET  /detail-invoices/:id} : get the "id" detailInvoice.
     *
     * @param id the id of the detailInvoice to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the detailInvoice, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/detail-invoices/{id}")
    public ResponseEntity<DetailInvoice> getDetailInvoice(@PathVariable Long id) {
        log.debug("REST request to get DetailInvoice : {}", id);
        Optional<DetailInvoice> detailInvoice = detailInvoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(detailInvoice);
    }

    /**
     * {@code DELETE  /detail-invoices/:id} : delete the "id" detailInvoice.
     *
     * @param id the id of the detailInvoice to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/detail-invoices/{id}")
    public ResponseEntity<Void> deleteDetailInvoice(@PathVariable Long id) {
        log.debug("REST request to delete DetailInvoice : {}", id);
        detailInvoiceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
