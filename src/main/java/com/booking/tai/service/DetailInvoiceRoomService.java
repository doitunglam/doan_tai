package com.booking.tai.service;

import com.booking.tai.domain.DetailInvoiceRoom;
import com.booking.tai.repository.DetailInvoiceRoomRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DetailInvoiceRoom}.
 */
@Service
@Transactional
public class DetailInvoiceRoomService {

    private final Logger log = LoggerFactory.getLogger(DetailInvoiceRoomService.class);

    private final DetailInvoiceRoomRepository detailInvoiceRoomRepository;

    public DetailInvoiceRoomService(DetailInvoiceRoomRepository detailInvoiceRoomRepository) {
        this.detailInvoiceRoomRepository = detailInvoiceRoomRepository;
    }

    /**
     * Save a detailInvoiceRoom.
     *
     * @param detailInvoiceRoom the entity to save.
     * @return the persisted entity.
     */
    public DetailInvoiceRoom save(DetailInvoiceRoom detailInvoiceRoom) {
        log.debug("Request to save DetailInvoiceRoom : {}", detailInvoiceRoom);
        return detailInvoiceRoomRepository.save(detailInvoiceRoom);
    }

    /**
     * Partially update a detailInvoiceRoom.
     *
     * @param detailInvoiceRoom the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DetailInvoiceRoom> partialUpdate(DetailInvoiceRoom detailInvoiceRoom) {
        log.debug("Request to partially update DetailInvoiceRoom : {}", detailInvoiceRoom);

        return detailInvoiceRoomRepository
            .findById(detailInvoiceRoom.getId())
            .map(existingDetailInvoiceRoom -> {
                if (detailInvoiceRoom.getPrice() != null) {
                    existingDetailInvoiceRoom.setPrice(detailInvoiceRoom.getPrice());
                }

                return existingDetailInvoiceRoom;
            })
            .map(detailInvoiceRoomRepository::save);
    }

    /**
     * Get all the detailInvoiceRooms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DetailInvoiceRoom> findAll() {
        log.debug("Request to get all DetailInvoiceRooms");
        return detailInvoiceRoomRepository.findAll();
    }

    /**
     * Get one detailInvoiceRoom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DetailInvoiceRoom> findOne(Long id) {
        log.debug("Request to get DetailInvoiceRoom : {}", id);
        return detailInvoiceRoomRepository.findById(id);
    }

    /**
     * Delete the detailInvoiceRoom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete DetailInvoiceRoom : {}", id);
        detailInvoiceRoomRepository.deleteById(id);
    }
}
