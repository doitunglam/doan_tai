package com.booking.tai.service;

import com.booking.tai.domain.TypeRoom;
import com.booking.tai.repository.TypeRoomRepository;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypeRoom}.
 */
@Service
@Transactional
public class TypeRoomService {

    private final Logger log = LoggerFactory.getLogger(TypeRoomService.class);

    private final TypeRoomRepository typeRoomRepository;

    public TypeRoomService(TypeRoomRepository typeRoomRepository) {
        this.typeRoomRepository = typeRoomRepository;
    }

    /**
     * Save a typeRoom.
     *
     * @param typeRoom the entity to save.
     * @return the persisted entity.
     */
    public TypeRoom save(TypeRoom typeRoom) {
        log.debug("Request to save TypeRoom : {}", typeRoom);
        return typeRoomRepository.save(typeRoom);
    }

    /**
     * Partially update a typeRoom.
     *
     * @param typeRoom the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TypeRoom> partialUpdate(TypeRoom typeRoom) {
        log.debug("Request to partially update TypeRoom : {}", typeRoom);

        return typeRoomRepository
            .findById(typeRoom.getId())
            .map(existingTypeRoom -> {
                if (typeRoom.getTypeName() != null) {
                    existingTypeRoom.setTypeName(typeRoom.getTypeName());
                }

                return existingTypeRoom;
            })
            .map(typeRoomRepository::save);
    }

    /**
     * Get all the typeRooms.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TypeRoom> findAll() {
        log.debug("Request to get all TypeRooms");
        return typeRoomRepository.findAll();
    }

    /**
     * Get one typeRoom by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TypeRoom> findOne(Long id) {
        log.debug("Request to get TypeRoom : {}", id);
        return typeRoomRepository.findById(id);
    }

    /**
     * Delete the typeRoom by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TypeRoom : {}", id);
        typeRoomRepository.deleteById(id);
    }
}
