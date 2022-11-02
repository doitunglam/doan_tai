package com.booking.tai.service;

import com.booking.tai.domain.PriceRoom;
import com.booking.tai.repository.PriceRoomRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PriceRoom}.
 */
@Service
@Transactional
public class PriceRoomService {

    private final Logger log = LoggerFactory.getLogger(PriceRoomService.class);

    private final PriceRoomRepository priceRoomRepository;

    public PriceRoomService(PriceRoomRepository priceRoomRepository) {
        this.priceRoomRepository = priceRoomRepository;
    }

    /**
     * Save a priceRoom.
     *
     * @param priceRoom the entity to save.
     * @return the persisted entity.
     */
    public PriceRoom save(PriceRoom priceRoom) {
        log.debug("Request to save PriceRoom : {}", priceRoom);
        return priceRoomRepository.save(priceRoom);
    }

    /**
     * Partially update a priceRoom.
     *
     * @param priceRoom the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PriceRoom> partialUpdate(PriceRoom priceRoom) {
        log.debug("Request to partially update PriceRoom : {}", priceRoom);

        return priceRoomRepository
            .findById(priceRoom.getId())
            .map(existingPriceRoom -> {
                if (priceRoom.getPrice() != null) {
                    existingPriceRoom.setPrice(priceRoom.getPrice());
                }
                if (priceRoom.getUnit() != null) {
                    existingPriceRoom.setUnit(priceRoom.getUnit());
                }

                return existingPriceRoom;
            })
            .map(priceRoomRepository::save);
    }

    /**
     * Get all the priceRooms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PriceRoom> findAll() {
        log.debug("Request to get all PriceRooms");
        return priceRoomRepository.findAll();
    }

    /**
     * Get one priceRoom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PriceRoom> findOne(Long id) {
        log.debug("Request to get PriceRoom : {}", id);
        return priceRoomRepository.findById(id);
    }

    /**
     * Delete the priceRoom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PriceRoom : {}", id);
        priceRoomRepository.deleteById(id);
    }
}
