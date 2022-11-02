package com.booking.tai.service;

import com.booking.tai.domain.ImageServices;
import com.booking.tai.repository.ImageServicesRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ImageServices}.
 */
@Service
@Transactional
public class ImageServicesService {

    private final Logger log = LoggerFactory.getLogger(ImageServicesService.class);

    private final ImageServicesRepository imageServicesRepository;

    public ImageServicesService(ImageServicesRepository imageServicesRepository) {
        this.imageServicesRepository = imageServicesRepository;
    }

    /**
     * Save a imageServices.
     *
     * @param imageServices the entity to save.
     * @return the persisted entity.
     */
    public ImageServices save(ImageServices imageServices) {
        log.debug("Request to save ImageServices : {}", imageServices);
        return imageServicesRepository.save(imageServices);
    }

    /**
     * Partially update a imageServices.
     *
     * @param imageServices the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ImageServices> partialUpdate(ImageServices imageServices) {
        log.debug("Request to partially update ImageServices : {}", imageServices);

        return imageServicesRepository
            .findById(imageServices.getId())
            .map(existingImageServices -> {
                if (imageServices.getLinkimage() != null) {
                    existingImageServices.setLinkimage(imageServices.getLinkimage());
                }

                return existingImageServices;
            })
            .map(imageServicesRepository::save);
    }

    /**
     * Get all the imageServices.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ImageServices> findAll() {
        log.debug("Request to get all ImageServices");
        return imageServicesRepository.findAll();
    }

    /**
     * Get one imageServices by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImageServices> findOne(Long id) {
        log.debug("Request to get ImageServices : {}", id);
        return imageServicesRepository.findById(id);
    }

    /**
     * Delete the imageServices by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ImageServices : {}", id);
        imageServicesRepository.deleteById(id);
    }
}
