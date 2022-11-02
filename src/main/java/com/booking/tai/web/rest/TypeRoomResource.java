package com.booking.tai.web.rest;

import com.booking.tai.domain.Hotel;
import com.booking.tai.domain.TypeRoom;
import com.booking.tai.domain.User;
import com.booking.tai.repository.HotelRepository;
import com.booking.tai.repository.RoomHotelRepository;
import com.booking.tai.repository.TypeRoomRepository;
import com.booking.tai.service.HotelService;
import com.booking.tai.service.TypeRoomService;
import com.booking.tai.service.UserService;
import com.booking.tai.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.booking.tai.domain.TypeRoom}.
 */
@RestController
@RequestMapping("/api")
public class TypeRoomResource {

    private final Logger log = LoggerFactory.getLogger(TypeRoomResource.class);

    private static final String ENTITY_NAME = "typeRoom";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TypeRoomService typeRoomService;

    private final TypeRoomRepository typeRoomRepository;

    private final RoomHotelRepository roomHotelRepository;

    private final HotelRepository hotelRepository;

    private final HotelService hotelService;

    private final UserService userService;

    public TypeRoomResource(TypeRoomService typeRoomService, TypeRoomRepository typeRoomRepository, RoomHotelRepository roomHotelRepository, HotelRepository hotelRepository, HotelService hotelService, UserService userService) {
        this.typeRoomService = typeRoomService;
        this.typeRoomRepository = typeRoomRepository;
        this.roomHotelRepository = roomHotelRepository;
        this.hotelRepository = hotelRepository;
        this.hotelService = hotelService;
        this.userService = userService;
    }

    /**
     * {@code POST  /type-rooms} : Create a new typeRoom.
     *
     * @param typeRoom the typeRoom to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new typeRoom, or with status {@code 400 (Bad Request)} if the typeRoom has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/manager/type-rooms")
    public ResponseEntity<TypeRoom> createTypeRoom(@RequestBody TypeRoom typeRoom) throws URISyntaxException {
        log.debug("REST request to save TypeRoom : {}", typeRoom);
        if (typeRoom.getId() != null) {
            throw new BadRequestAlertException("A new typeRoom cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User manager = userService.getUserWithAuthorities().get();
        Hotel hotel = hotelService.findOne(typeRoom.getHotel().getId()).get();
        if(hotelRepository.findByUserAndHotelId(typeRoom.getHotel().getId(), manager.getId()).isEmpty()) {
            throw new BadRequestAlertException("access denied", ENTITY_NAME, "check your account");
        }
        TypeRoom result = typeRoomService.save(typeRoom);
        return ResponseEntity
            .created(new URI("/api/type-rooms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /type-rooms/:id} : Updates an existing typeRoom.
     *
     * @param id the id of the typeRoom to save.
     * @param typeRoom the typeRoom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeRoom,
     * or with status {@code 400 (Bad Request)} if the typeRoom is not valid,
     * or with status {@code 500 (Internal Server Error)} if the typeRoom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/type-rooms/{id}")
    public ResponseEntity<TypeRoom> updateTypeRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeRoom typeRoom
    ) throws URISyntaxException {
        log.debug("REST request to update TypeRoom : {}, {}", id, typeRoom);
        if (typeRoom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeRoom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TypeRoom result = typeRoomService.save(typeRoom);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeRoom.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /type-rooms/:id} : Partial updates given fields of an existing typeRoom, field will ignore if it is null
     *
     * @param id the id of the typeRoom to save.
     * @param typeRoom the typeRoom to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated typeRoom,
     * or with status {@code 400 (Bad Request)} if the typeRoom is not valid,
     * or with status {@code 404 (Not Found)} if the typeRoom is not found,
     * or with status {@code 500 (Internal Server Error)} if the typeRoom couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/type-rooms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TypeRoom> partialUpdateTypeRoom(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TypeRoom typeRoom
    ) throws URISyntaxException {
        log.debug("REST request to partial update TypeRoom partially : {}, {}", id, typeRoom);
        if (typeRoom.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, typeRoom.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!typeRoomRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TypeRoom> result = typeRoomService.partialUpdate(typeRoom);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, typeRoom.getId().toString())
        );
    }

    /**
     * {@code GET  /type-rooms} : get all the typeRooms.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of typeRooms in body.
     */
    @GetMapping("/type-rooms")
    public List<TypeRoom> getAllTypeRooms() {
        log.debug("REST request to get all TypeRooms");
        return typeRoomService.findAll();
    }

    /**
     * {@code GET  /type-rooms/:id} : get the "id" typeRoom.
     *
     * @param id the id of the typeRoom to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the typeRoom, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/public/type-rooms/{id}")
    public ResponseEntity<TypeRoom> getTypeRoom(@PathVariable Long id) {
        log.debug("REST request to get TypeRoom : {}", id);
        Optional<TypeRoom> typeRoom = typeRoomService.findOne(id);
        return ResponseUtil.wrapOrNotFound(typeRoom);
    }

    /**
     * {@code DELETE  /type-rooms/:id} : delete the "id" typeRoom.
     *
     * @param id the id of the typeRoom to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/manager/type-rooms/{id}")
    public ResponseEntity<Void> deleteTypeRoom(@PathVariable Long id) {
        log.debug("REST request to delete TypeRoom : {}", id);
        typeRoomService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
