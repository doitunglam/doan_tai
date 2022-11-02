package com.booking.tai.service;

import com.booking.tai.domain.RoomHotel;
import com.booking.tai.repository.RoomHotelRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link RoomHotel}.
 */
@Service
@Transactional
public class RoomHotelService {

    private final Logger log = LoggerFactory.getLogger(RoomHotelService.class);

    private final RoomHotelRepository roomHotelRepository;

    public RoomHotelService(RoomHotelRepository roomHotelRepository) {
        this.roomHotelRepository = roomHotelRepository;
    }

    /**
     * Save a roomHotel.
     *
     * @param roomHotel the entity to save.
     * @return the persisted entity.
     */
    public RoomHotel save(RoomHotel roomHotel) {
        log.debug("Request to save RoomHotel : {}", roomHotel);
        return roomHotelRepository.save(roomHotel);
    }

    /**
     * Partially update a roomHotel.
     *
     * @param roomHotel the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RoomHotel> partialUpdate(RoomHotel roomHotel) {
        log.debug("Request to partially update RoomHotel : {}", roomHotel);

        return roomHotelRepository
            .findById(roomHotel.getId())
            .map(existingRoomHotel -> {
                if (roomHotel.getRoomName() != null) {
                    existingRoomHotel.setRoomName(roomHotel.getRoomName());
                }
                if (roomHotel.getDescription() != null) {
                    existingRoomHotel.setDescription(roomHotel.getDescription());
                }
                if (roomHotel.getRoomStatus() != null) {
                    existingRoomHotel.setRoomStatus(roomHotel.getRoomStatus());
                }

                return existingRoomHotel;
            })
            .map(roomHotelRepository::save);
    }

    /**
     * Get all the roomHotels.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RoomHotel> findAll() {
        log.debug("Request to get all RoomHotels");
        return roomHotelRepository.findAll();
    }

    /**
     * Get one roomHotel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RoomHotel> findOne(Long id) {
        log.debug("Request to get RoomHotel : {}", id);
        return roomHotelRepository.findById(id);
    }

    /**
     * Delete the roomHotel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RoomHotel : {}", id);
        roomHotelRepository.deleteById(id);
    }
}
