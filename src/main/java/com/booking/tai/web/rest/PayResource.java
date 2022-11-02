package com.booking.tai.web.rest;

import com.booking.tai.domain.Pay;
import com.booking.tai.repository.PayRepository;
import com.booking.tai.service.PayService;
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
 * REST controller for managing {@link com.booking.tai.domain.Pay}.
 */
@RestController
@RequestMapping("/api")
public class PayResource {

    private final Logger log = LoggerFactory.getLogger(PayResource.class);

    private static final String ENTITY_NAME = "pay";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PayService payService;

    private final PayRepository payRepository;

    public PayResource(PayService payService, PayRepository payRepository) {
        this.payService = payService;
        this.payRepository = payRepository;
    }

    /**
     * {@code POST  /pays} : Create a new pay.
     *
     * @param pay the pay to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pay, or with status {@code 400 (Bad Request)} if the pay has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pays")
    public ResponseEntity<Pay> createPay(@RequestBody Pay pay) throws URISyntaxException {
        log.debug("REST request to save Pay : {}", pay);
        if (pay.getId() != null) {
            throw new BadRequestAlertException("A new pay cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Pay result = payService.save(pay);
        return ResponseEntity
            .created(new URI("/api/pays/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pays/:id} : Updates an existing pay.
     *
     * @param id the id of the pay to save.
     * @param pay the pay to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pay,
     * or with status {@code 400 (Bad Request)} if the pay is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pay couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pays/{id}")
    public ResponseEntity<Pay> updatePay(@PathVariable(value = "id", required = false) final Long id, @RequestBody Pay pay)
        throws URISyntaxException {
        log.debug("REST request to update Pay : {}, {}", id, pay);
        if (pay.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pay.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!payRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Pay result = payService.save(pay);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pay.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pays/:id} : Partial updates given fields of an existing pay, field will ignore if it is null
     *
     * @param id the id of the pay to save.
     * @param pay the pay to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pay,
     * or with status {@code 400 (Bad Request)} if the pay is not valid,
     * or with status {@code 404 (Not Found)} if the pay is not found,
     * or with status {@code 500 (Internal Server Error)} if the pay couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pays/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Pay> partialUpdatePay(@PathVariable(value = "id", required = false) final Long id, @RequestBody Pay pay)
        throws URISyntaxException {
        log.debug("REST request to partial update Pay partially : {}, {}", id, pay);
        if (pay.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pay.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!payRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Pay> result = payService.partialUpdate(pay);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pay.getId().toString())
        );
    }

    /**
     * {@code GET  /pays} : get all the pays.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pays in body.
     */
    @GetMapping("/pays")
    public List<Pay> getAllPays(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Pays");
        return payService.findAll();
    }

    /**
     * {@code GET  /pays/:id} : get the "id" pay.
     *
     * @param id the id of the pay to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pay, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pays/{id}")
    public ResponseEntity<Pay> getPay(@PathVariable Long id) {
        log.debug("REST request to get Pay : {}", id);
        Optional<Pay> pay = payService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pay);
    }

    /**
     * {@code DELETE  /pays/:id} : delete the "id" pay.
     *
     * @param id the id of the pay to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pays/{id}")
    public ResponseEntity<Void> deletePay(@PathVariable Long id) {
        log.debug("REST request to delete Pay : {}", id);
        payService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
