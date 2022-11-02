package com.booking.tai.service;

import com.booking.tai.domain.Hotel;
import com.booking.tai.repository.HotelRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Hotel}.
 */
@Service
@Transactional
public class HotelService {

    private final Logger log = LoggerFactory.getLogger(HotelService.class);

    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    /**
     * Save a hotel.
     *
     * @param hotel the entity to save.
     * @return the persisted entity.
     */
    public Hotel save(Hotel hotel) {
        log.debug("Request to save Hotel : {}", hotel);
        return hotelRepository.save(hotel);
    }

    /**
     * Partially update a hotel.
     *
     * @param hotel the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Hotel> partialUpdate(Hotel hotel) {
        log.debug("Request to partially update Hotel : {}", hotel);

        return hotelRepository
            .findById(hotel.getId())
            .map(existingHotel -> {
                if (hotel.getHotelName() != null) {
                    existingHotel.setHotelName(hotel.getHotelName());
                }
                if (hotel.getAddress() != null) {
                    existingHotel.setAddress(hotel.getAddress());
                }
                if (hotel.getPhone() != null) {
                    existingHotel.setPhone(hotel.getPhone());
                }
                if (hotel.getToken() != null) {
                    existingHotel.setToken(hotel.getToken());
                }
                if (hotel.getActive() != null) {
                    existingHotel.setActive(hotel.getActive());
                }

                return existingHotel;
            })
            .map(hotelRepository::save);
    }

    /**
     * Get all the hotels.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Hotel> findAll() {
        log.debug("Request to get all Hotels");
        return hotelRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the hotels with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Hotel> findAllWithEagerRelationships(Pageable pageable) {
        return hotelRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one hotel by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Hotel> findOne(Long id) {
        log.debug("Request to get Hotel : {}", id);
        return hotelRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the hotel by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Hotel : {}", id);
        hotelRepository.deleteById(id);
    }
}
