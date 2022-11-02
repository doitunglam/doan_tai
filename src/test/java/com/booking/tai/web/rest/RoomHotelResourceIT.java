package com.booking.tai.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.booking.tai.IntegrationTest;
import com.booking.tai.domain.RoomHotel;
import com.booking.tai.repository.RoomHotelRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RoomHotelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RoomHotelResourceIT {

    private static final String DEFAULT_ROOM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ROOM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_ROOM_STATUS = 1;
    private static final Integer UPDATED_ROOM_STATUS = 2;

    private static final String ENTITY_API_URL = "/api/room-hotels";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RoomHotelRepository roomHotelRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRoomHotelMockMvc;

    private RoomHotel roomHotel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomHotel createEntity(EntityManager em) {
        RoomHotel roomHotel = new RoomHotel().roomName(DEFAULT_ROOM_NAME).description(DEFAULT_DESCRIPTION).roomStatus(DEFAULT_ROOM_STATUS);
        return roomHotel;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RoomHotel createUpdatedEntity(EntityManager em) {
        RoomHotel roomHotel = new RoomHotel().roomName(UPDATED_ROOM_NAME).description(UPDATED_DESCRIPTION).roomStatus(UPDATED_ROOM_STATUS);
        return roomHotel;
    }

    @BeforeEach
    public void initTest() {
        roomHotel = createEntity(em);
    }

    @Test
    @Transactional
    void createRoomHotel() throws Exception {
        int databaseSizeBeforeCreate = roomHotelRepository.findAll().size();
        // Create the RoomHotel
        restRoomHotelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomHotel)))
            .andExpect(status().isCreated());

        // Validate the RoomHotel in the database
        List<RoomHotel> roomHotelList = roomHotelRepository.findAll();
        assertThat(roomHotelList).hasSize(databaseSizeBeforeCreate + 1);
        RoomHotel testRoomHotel = roomHotelList.get(roomHotelList.size() - 1);
        assertThat(testRoomHotel.getRoomName()).isEqualTo(DEFAULT_ROOM_NAME);
        assertThat(testRoomHotel.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testRoomHotel.getRoomStatus()).isEqualTo(DEFAULT_ROOM_STATUS);
    }

    @Test
    @Transactional
    void createRoomHotelWithExistingId() throws Exception {
        // Create the RoomHotel with an existing ID
        roomHotel.setId(1L);

        int databaseSizeBeforeCreate = roomHotelRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRoomHotelMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomHotel)))
            .andExpect(status().isBadRequest());

        // Validate the RoomHotel in the database
        List<RoomHotel> roomHotelList = roomHotelRepository.findAll();
        assertThat(roomHotelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRoomHotels() throws Exception {
        // Initialize the database
        roomHotelRepository.saveAndFlush(roomHotel);

        // Get all the roomHotelList
        restRoomHotelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(roomHotel.getId().intValue())))
            .andExpect(jsonPath("$.[*].roomName").value(hasItem(DEFAULT_ROOM_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].roomStatus").value(hasItem(DEFAULT_ROOM_STATUS)));
    }

    @Test
    @Transactional
    void getRoomHotel() throws Exception {
        // Initialize the database
        roomHotelRepository.saveAndFlush(roomHotel);

        // Get the roomHotel
        restRoomHotelMockMvc
            .perform(get(ENTITY_API_URL_ID, roomHotel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(roomHotel.getId().intValue()))
            .andExpect(jsonPath("$.roomName").value(DEFAULT_ROOM_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.roomStatus").value(DEFAULT_ROOM_STATUS));
    }

    @Test
    @Transactional
    void getNonExistingRoomHotel() throws Exception {
        // Get the roomHotel
        restRoomHotelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRoomHotel() throws Exception {
        // Initialize the database
        roomHotelRepository.saveAndFlush(roomHotel);

        int databaseSizeBeforeUpdate = roomHotelRepository.findAll().size();

        // Update the roomHotel
        RoomHotel updatedRoomHotel = roomHotelRepository.findById(roomHotel.getId()).get();
        // Disconnect from session so that the updates on updatedRoomHotel are not directly saved in db
        em.detach(updatedRoomHotel);
        updatedRoomHotel.roomName(UPDATED_ROOM_NAME).description(UPDATED_DESCRIPTION).roomStatus(UPDATED_ROOM_STATUS);

        restRoomHotelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRoomHotel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRoomHotel))
            )
            .andExpect(status().isOk());

        // Validate the RoomHotel in the database
        List<RoomHotel> roomHotelList = roomHotelRepository.findAll();
        assertThat(roomHotelList).hasSize(databaseSizeBeforeUpdate);
        RoomHotel testRoomHotel = roomHotelList.get(roomHotelList.size() - 1);
        assertThat(testRoomHotel.getRoomName()).isEqualTo(UPDATED_ROOM_NAME);
        assertThat(testRoomHotel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRoomHotel.getRoomStatus()).isEqualTo(UPDATED_ROOM_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingRoomHotel() throws Exception {
        int databaseSizeBeforeUpdate = roomHotelRepository.findAll().size();
        roomHotel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomHotelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, roomHotel.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomHotel))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomHotel in the database
        List<RoomHotel> roomHotelList = roomHotelRepository.findAll();
        assertThat(roomHotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRoomHotel() throws Exception {
        int databaseSizeBeforeUpdate = roomHotelRepository.findAll().size();
        roomHotel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomHotelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(roomHotel))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomHotel in the database
        List<RoomHotel> roomHotelList = roomHotelRepository.findAll();
        assertThat(roomHotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRoomHotel() throws Exception {
        int databaseSizeBeforeUpdate = roomHotelRepository.findAll().size();
        roomHotel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomHotelMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(roomHotel)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomHotel in the database
        List<RoomHotel> roomHotelList = roomHotelRepository.findAll();
        assertThat(roomHotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRoomHotelWithPatch() throws Exception {
        // Initialize the database
        roomHotelRepository.saveAndFlush(roomHotel);

        int databaseSizeBeforeUpdate = roomHotelRepository.findAll().size();

        // Update the roomHotel using partial update
        RoomHotel partialUpdatedRoomHotel = new RoomHotel();
        partialUpdatedRoomHotel.setId(roomHotel.getId());

        partialUpdatedRoomHotel.roomName(UPDATED_ROOM_NAME).description(UPDATED_DESCRIPTION);

        restRoomHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomHotel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomHotel))
            )
            .andExpect(status().isOk());

        // Validate the RoomHotel in the database
        List<RoomHotel> roomHotelList = roomHotelRepository.findAll();
        assertThat(roomHotelList).hasSize(databaseSizeBeforeUpdate);
        RoomHotel testRoomHotel = roomHotelList.get(roomHotelList.size() - 1);
        assertThat(testRoomHotel.getRoomName()).isEqualTo(UPDATED_ROOM_NAME);
        assertThat(testRoomHotel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRoomHotel.getRoomStatus()).isEqualTo(DEFAULT_ROOM_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateRoomHotelWithPatch() throws Exception {
        // Initialize the database
        roomHotelRepository.saveAndFlush(roomHotel);

        int databaseSizeBeforeUpdate = roomHotelRepository.findAll().size();

        // Update the roomHotel using partial update
        RoomHotel partialUpdatedRoomHotel = new RoomHotel();
        partialUpdatedRoomHotel.setId(roomHotel.getId());

        partialUpdatedRoomHotel.roomName(UPDATED_ROOM_NAME).description(UPDATED_DESCRIPTION).roomStatus(UPDATED_ROOM_STATUS);

        restRoomHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRoomHotel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRoomHotel))
            )
            .andExpect(status().isOk());

        // Validate the RoomHotel in the database
        List<RoomHotel> roomHotelList = roomHotelRepository.findAll();
        assertThat(roomHotelList).hasSize(databaseSizeBeforeUpdate);
        RoomHotel testRoomHotel = roomHotelList.get(roomHotelList.size() - 1);
        assertThat(testRoomHotel.getRoomName()).isEqualTo(UPDATED_ROOM_NAME);
        assertThat(testRoomHotel.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testRoomHotel.getRoomStatus()).isEqualTo(UPDATED_ROOM_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingRoomHotel() throws Exception {
        int databaseSizeBeforeUpdate = roomHotelRepository.findAll().size();
        roomHotel.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRoomHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, roomHotel.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomHotel))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomHotel in the database
        List<RoomHotel> roomHotelList = roomHotelRepository.findAll();
        assertThat(roomHotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRoomHotel() throws Exception {
        int databaseSizeBeforeUpdate = roomHotelRepository.findAll().size();
        roomHotel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomHotelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(roomHotel))
            )
            .andExpect(status().isBadRequest());

        // Validate the RoomHotel in the database
        List<RoomHotel> roomHotelList = roomHotelRepository.findAll();
        assertThat(roomHotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRoomHotel() throws Exception {
        int databaseSizeBeforeUpdate = roomHotelRepository.findAll().size();
        roomHotel.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRoomHotelMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(roomHotel))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RoomHotel in the database
        List<RoomHotel> roomHotelList = roomHotelRepository.findAll();
        assertThat(roomHotelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRoomHotel() throws Exception {
        // Initialize the database
        roomHotelRepository.saveAndFlush(roomHotel);

        int databaseSizeBeforeDelete = roomHotelRepository.findAll().size();

        // Delete the roomHotel
        restRoomHotelMockMvc
            .perform(delete(ENTITY_API_URL_ID, roomHotel.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RoomHotel> roomHotelList = roomHotelRepository.findAll();
        assertThat(roomHotelList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
