package com.booking.tai.service;

import com.booking.tai.domain.Pay;
import com.booking.tai.repository.PayRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pay}.
 */
@Service
@Transactional
public class PayService {

    private final Logger log = LoggerFactory.getLogger(PayService.class);

    private final PayRepository payRepository;

    public PayService(PayRepository payRepository) {
        this.payRepository = payRepository;
    }

    /**
     * Save a pay.
     *
     * @param pay the entity to save.
     * @return the persisted entity.
     */
    public Pay save(Pay pay) {
        log.debug("Request to save Pay : {}", pay);
        return payRepository.save(pay);
    }

    /**
     * Partially update a pay.
     *
     * @param pay the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Pay> partialUpdate(Pay pay) {
        log.debug("Request to partially update Pay : {}", pay);

        return payRepository
            .findById(pay.getId())
            .map(existingPay -> {
                if (pay.getCode() != null) {
                    existingPay.setCode(pay.getCode());
                }
                if (pay.getCost() != null) {
                    existingPay.setCost(pay.getCost());
                }

                return existingPay;
            })
            .map(payRepository::save);
    }

    /**
     * Get all the pays.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Pay> findAll() {
        log.debug("Request to get all Pays");
        return payRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the pays with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Pay> findAllWithEagerRelationships(Pageable pageable) {
        return payRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one pay by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Pay> findOne(Long id) {
        log.debug("Request to get Pay : {}", id);
        return payRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the pay by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Pay : {}", id);
        payRepository.deleteById(id);
    }
}
