package com.booking.tai.web.rest;

import com.booking.tai.domain.RoomHotel;
import com.booking.tai.domain.TypeRoom;
import com.booking.tai.domain.User;
import com.booking.tai.repository.HotelRepository;
import com.booking.tai.repository.RoomHotelRepository;
import com.booking.tai.service.RoomHotelService;
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
 * REST controller for managing {@link com.booking.tai.domain.RoomHotel}.
 */
@RestController
@RequestMapping("/api")
public class RoomHotelResource {

    private final Logger log = LoggerFactory.getLogger(RoomHotelResource.class);

    private static final String ENTITY_NAME = "roomHotel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RoomHotelService roomHotelService;

    private final RoomHotelRepository roomHotelRepository;

    private final UserService userService;

    private final TypeRoomService typeRoomService;

    private final HotelRepository hotelRepository;

    public RoomHotelResource(RoomHotelService roomHotelService, RoomHotelRepository roomHotelRepository, UserService userService, TypeRoomService typeRoomService, HotelRepository hotelRepository) {
        this.roomHotelService = roomHotelService;
        this.roomHotelRepository = roomHotelRepository;
        this.userService = userService;
        this.typeRoomService = typeRoomService;
        this.hotelRepository = hotelRepository;
    }

    /**
     * {@code POST  /room-hotels} : Create a new roomHotel.
     *
     * @param roomHotel the roomHotel to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new roomHotel, or with status {@code 400 (Bad Request)} if the roomHotel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/manager/room-hotels")
    public ResponseEntity<RoomHotel> createRoomHotel(@RequestBody RoomHotel roomHotel) throws URISyntaxException {
        log.debug("REST request to save RoomHotel : {}", roomHotel);
        if (roomHotel.getId() != null) {
            throw new BadRequestAlertException("A new roomHotel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        User manager = userService.getUserWithAuthorities().get();
        TypeRoom typeRoom = typeRoomService.findOne(roomHotel.getTypeRoom().getId()).get();
        if(hotelRepository.findByUserAndHotelId(typeRoom.getHotel().getId(), manager.getId()).isEmpty()){
            throw new BadRequestAlertException("Access dineid", ENTITY_NAME, "check your account");
        }
        RoomHotel result = roomHotelService.save(roomHotel);
        return ResponseEntity
            .created(new URI("/api/room-hotels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("/manager/update-room-hotels/{id}")
    public ResponseEntity<RoomHotel> updateRoomHotel(@PathVariable(value = "id") Long id,@RequestBody RoomHotel roomHotel) throws URISyntaxException {
        log.debug("REST request to save RoomHotel : {}", roomHotel);
        User manager = userService.getUserWithAuthorities().get();
        TypeRoom typeRoom = typeRoomService.findOne(roomHotel.getTypeRoom().getId()).get();
        if(hotelRepository.findByUserAndHotelId(typeRoom.getHotel().getId(), manager.getId()).isEmpty()){
            throw new BadRequestAlertException("Access dineid", ENTITY_NAME, "check your account");
        }
        RoomHotel result = roomHotelService.save(roomHotel);
        return ResponseEntity
            .created(new URI("/api/room-hotels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }


    /**
     * {@code PATCH  /room-hotels/:id} : Partial updates given fields of an existing roomHotel, field will ignore if it is null
     *
     * @param id the id of the roomHotel to save.
     * @param roomHotel the roomHotel to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated roomHotel,
     * or with status {@code 400 (Bad Request)} if the roomHotel is not valid,
     * or with status {@code 404 (Not Found)} if the roomHotel is not found,
     * or with status {@code 500 (Internal Server Error)} if the roomHotel couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/room-hotels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RoomHotel> partialUpdateRoomHotel(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RoomHotel roomHotel
    ) throws URISyntaxException {
        log.debug("REST request to partial update RoomHotel partially : {}, {}", id, roomHotel);
        if (roomHotel.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, roomHotel.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roomHotelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RoomHotel> result = roomHotelService.partialUpdate(roomHotel);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, roomHotel.getId().toString())
        );
    }

    /**
     * {@code GET  /room-hotels} : get all the roomHotels.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roomHotels in body.
     */
    @GetMapping("/room-hotels")
    public List<RoomHotel> getAllRoomHotels() {
        log.debug("REST request to get all RoomHotels");
        return roomHotelService.findAll();
    }

    /**
     * {@code GET  /room-hotels/:id} : get the "id" roomHotel.
     *
     * @param id the id of the roomHotel to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the roomHotel, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/room-hotels/{id}")
    public ResponseEntity<RoomHotel> getRoomHotel(@PathVariable Long id) {
        log.debug("REST request to get RoomHotel : {}", id);
        Optional<RoomHotel> roomHotel = roomHotelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(roomHotel);
    }

    /**
     * {@code DELETE  /room-hotels/:id} : delete the "id" roomHotel.
     *
     * @param id the id of the roomHotel to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/manager/room-hotels/{id}")
    public ResponseEntity<Void> deleteRoomHotel(@PathVariable Long id) {
        log.debug("REST request to delete RoomHotel : {}", id);
        RoomHotel roomHotel = roomHotelService.findOne(id).get();
        User manager = userService.getUserWithAuthorities().get();
        TypeRoom typeRoom = typeRoomService.findOne(roomHotel.getTypeRoom().getId()).get();
        if(hotelRepository.findByUserAndHotelId(typeRoom.getHotel().getId(), manager.getId()).isEmpty()){
            throw new BadRequestAlertException("Access dineid", ENTITY_NAME, "check your account");
        }
        roomHotelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
