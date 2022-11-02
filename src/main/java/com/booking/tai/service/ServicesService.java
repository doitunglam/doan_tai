package com.booking.tai.service;

import com.booking.tai.domain.Services;
import com.booking.tai.repository.ServicesRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Services}.
 */
@Service
@Transactional
public class ServicesService {

    private final Logger log = LoggerFactory.getLogger(ServicesService.class);

    private final ServicesRepository servicesRepository;

    public ServicesService(ServicesRepository servicesRepository) {
        this.servicesRepository = servicesRepository;
    }

    /**
     * Save a services.
     *
     * @param services the entity to save.
     * @return the persisted entity.
     */
    public Services save(Services services) {
        log.debug("Request to save Services : {}", services);
        return servicesRepository.save(services);
    }

    /**
     * Partially update a services.
     *
     * @param services the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Services> partialUpdate(Services services) {
        log.debug("Request to partially update Services : {}", services);

        return servicesRepository
            .findById(services.getId())
            .map(existingServices -> {
                if (services.getServiceName() != null) {
                    existingServices.setServiceName(services.getServiceName());
                }
                if (services.getPrice() != null) {
                    existingServices.setPrice(services.getPrice());
                }
                if (services.getUnit() != null) {
                    existingServices.setUnit(services.getUnit());
                }
                if (services.getQuantity() != null) {
                    existingServices.setQuantity(services.getQuantity());
                }
                if (services.getAddress() != null) {
                    existingServices.setAddress(services.getAddress());
                }

                return existingServices;
            })
            .map(servicesRepository::save);
    }

    /**
     * Get all the services.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Services> findAll() {
        log.debug("Request to get all Services");
        return servicesRepository.findAll();
    }

    /**
     * Get one services by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Services> findOne(Long id) {
        log.debug("Request to get Services : {}", id);
        return servicesRepository.findById(id);
    }

    /**
     * Delete the services by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Services : {}", id);
        servicesRepository.deleteById(id);
    }
}
