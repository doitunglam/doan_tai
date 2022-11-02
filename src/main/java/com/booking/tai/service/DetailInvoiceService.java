package com.booking.tai.service;

import com.booking.tai.domain.DetailInvoice;
import com.booking.tai.repository.DetailInvoiceRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DetailInvoice}.
 */
@Service
@Transactional
public class DetailInvoiceService {

    private final Logger log = LoggerFactory.getLogger(DetailInvoiceService.class);

    private final DetailInvoiceRepository detailInvoiceRepository;

    public DetailInvoiceService(DetailInvoiceRepository detailInvoiceRepository) {
        this.detailInvoiceRepository = detailInvoiceRepository;
    }

    /**
     * Save a detailInvoice.
     *
     * @param detailInvoice the entity to save.
     * @return the persisted entity.
     */
    public DetailInvoice save(DetailInvoice detailInvoice) {
        log.debug("Request to save DetailInvoice : {}", detailInvoice);
        return detailInvoiceRepository.save(detailInvoice);
    }

    /**
     * Partially update a detailInvoice.
     *
     * @param detailInvoice the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DetailInvoice> partialUpdate(DetailInvoice detailInvoice) {
        log.debug("Request to partially update DetailInvoice : {}", detailInvoice);

        return detailInvoiceRepository
            .findById(detailInvoice.getId())
            .map(existingDetailInvoice -> {
                if (detailInvoice.getQuantity() != null) {
                    existingDetailInvoice.setQuantity(detailInvoice.getQuantity());
                }
                if (detailInvoice.getPrice() != null) {
                    existingDetailInvoice.setPrice(detailInvoice.getPrice());
                }

                return existingDetailInvoice;
            })
            .map(detailInvoiceRepository::save);
    }

    /**
     * Get all the detailInvoices.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DetailInvoice> findAll() {
        log.debug("Request to get all DetailInvoices");
        return detailInvoiceRepository.findAll();
    }

    /**
     * Get one detailInvoice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DetailInvoice> findOne(Long id) {
        log.debug("Request to get DetailInvoice : {}", id);
        return detailInvoiceRepository.findById(id);
    }

    /**
     * Delete the detailInvoice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DetailInvoice : {}", id);
        detailInvoiceRepository.deleteById(id);
    }
}
