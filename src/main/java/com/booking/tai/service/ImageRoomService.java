package com.booking.tai.service;

import com.booking.tai.domain.ImageRoom;
import com.booking.tai.repository.ImageRoomRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ImageRoom}.
 */
@Service
@Transactional
public class ImageRoomService {

    private final Logger log = LoggerFactory.getLogger(ImageRoomService.class);

    private final ImageRoomRepository imageRoomRepository;

    public ImageRoomService(ImageRoomRepository imageRoomRepository) {
        this.imageRoomRepository = imageRoomRepository;
    }

    /**
     * Save a imageRoom.
     *
     * @param imageRoom the entity to save.
     * @return the persisted entity.
     */
    public ImageRoom save(ImageRoom imageRoom) {
        log.debug("Request to save ImageRoom : {}", imageRoom);
        return imageRoomRepository.save(imageRoom);
    }

    /**
     * Partially update a imageRoom.
     *
     * @param imageRoom the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ImageRoom> partialUpdate(ImageRoom imageRoom) {
        log.debug("Request to partially update ImageRoom : {}", imageRoom);

        return imageRoomRepository
            .findById(imageRoom.getId())
            .map(existingImageRoom -> {
                if (imageRoom.getLinkImage() != null) {
                    existingImageRoom.setLinkImage(imageRoom.getLinkImage());
                }

                return existingImageRoom;
            })
            .map(imageRoomRepository::save);
    }

    /**
     * Get all the imageRooms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ImageRoom> findAll() {
        log.debug("Request to get all ImageRooms");
        return imageRoomRepository.findAll();
    }

    /**
     * Get one imageRoom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImageRoom> findOne(Long id) {
        log.debug("Request to get ImageRoom : {}", id);
        return imageRoomRepository.findById(id);
    }

    /**
     * Delete the imageRoom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ImageRoom : {}", id);
        imageRoomRepository.deleteById(id);
    }
}
