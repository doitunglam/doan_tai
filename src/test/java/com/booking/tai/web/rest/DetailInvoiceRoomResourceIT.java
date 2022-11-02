package com.booking.tai.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.booking.tai.IntegrationTest;
import com.booking.tai.domain.DetailInvoiceRoom;
import com.booking.tai.repository.DetailInvoiceRoomRepository;
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
 * Integration tests for the {@link DetailInvoiceRoomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetailInvoiceRoomResourceIT {

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/detail-invoice-rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetailInvoiceRoomRepository detailInvoiceRoomRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetailInvoiceRoomMockMvc;

    private DetailInvoiceRoom detailInvoiceRoom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailInvoiceRoom createEntity(EntityManager em) {
        DetailInvoiceRoom detailInvoiceRoom = new DetailInvoiceRoom().price(DEFAULT_PRICE);
        return detailInvoiceRoom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailInvoiceRoom createUpdatedEntity(EntityManager em) {
        DetailInvoiceRoom detailInvoiceRoom = new DetailInvoiceRoom().price(UPDATED_PRICE);
        return detailInvoiceRoom;
    }

    @BeforeEach
    public void initTest() {
        detailInvoiceRoom = createEntity(em);
    }

    @Test
    @Transactional
    void createDetailInvoiceRoom() throws Exception {
        int databaseSizeBeforeCreate = detailInvoiceRoomRepository.findAll().size();
        // Create the DetailInvoiceRoom
        restDetailInvoiceRoomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailInvoiceRoom))
            )
            .andExpect(status().isCreated());

        // Validate the DetailInvoiceRoom in the database
        List<DetailInvoiceRoom> detailInvoiceRoomList = detailInvoiceRoomRepository.findAll();
        assertThat(detailInvoiceRoomList).hasSize(databaseSizeBeforeCreate + 1);
        DetailInvoiceRoom testDetailInvoiceRoom = detailInvoiceRoomList.get(detailInvoiceRoomList.size() - 1);
        assertThat(testDetailInvoiceRoom.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createDetailInvoiceRoomWithExistingId() throws Exception {
        // Create the DetailInvoiceRoom with an existing ID
        detailInvoiceRoom.setId(1L);

        int databaseSizeBeforeCreate = detailInvoiceRoomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetailInvoiceRoomMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailInvoiceRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailInvoiceRoom in the database
        List<DetailInvoiceRoom> detailInvoiceRoomList = detailInvoiceRoomRepository.findAll();
        assertThat(detailInvoiceRoomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDetailInvoiceRooms() throws Exception {
        // Initialize the database
        detailInvoiceRoomRepository.saveAndFlush(detailInvoiceRoom);

        // Get all the detailInvoiceRoomList
        restDetailInvoiceRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailInvoiceRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getDetailInvoiceRoom() throws Exception {
        // Initialize the database
        detailInvoiceRoomRepository.saveAndFlush(detailInvoiceRoom);

        // Get the detailInvoiceRoom
        restDetailInvoiceRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, detailInvoiceRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detailInvoiceRoom.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingDetailInvoiceRoom() throws Exception {
        // Get the detailInvoiceRoom
        restDetailInvoiceRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDetailInvoiceRoom() throws Exception {
        // Initialize the database
        detailInvoiceRoomRepository.saveAndFlush(detailInvoiceRoom);

        int databaseSizeBeforeUpdate = detailInvoiceRoomRepository.findAll().size();

        // Update the detailInvoiceRoom
        DetailInvoiceRoom updatedDetailInvoiceRoom = detailInvoiceRoomRepository.findById(detailInvoiceRoom.getId()).get();
        // Disconnect from session so that the updates on updatedDetailInvoiceRoom are not directly saved in db
        em.detach(updatedDetailInvoiceRoom);
        updatedDetailInvoiceRoom.price(UPDATED_PRICE);

        restDetailInvoiceRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDetailInvoiceRoom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDetailInvoiceRoom))
            )
            .andExpect(status().isOk());

        // Validate the DetailInvoiceRoom in the database
        List<DetailInvoiceRoom> detailInvoiceRoomList = detailInvoiceRoomRepository.findAll();
        assertThat(detailInvoiceRoomList).hasSize(databaseSizeBeforeUpdate);
        DetailInvoiceRoom testDetailInvoiceRoom = detailInvoiceRoomList.get(detailInvoiceRoomList.size() - 1);
        assertThat(testDetailInvoiceRoom.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingDetailInvoiceRoom() throws Exception {
        int databaseSizeBeforeUpdate = detailInvoiceRoomRepository.findAll().size();
        detailInvoiceRoom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailInvoiceRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detailInvoiceRoom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailInvoiceRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailInvoiceRoom in the database
        List<DetailInvoiceRoom> detailInvoiceRoomList = detailInvoiceRoomRepository.findAll();
        assertThat(detailInvoiceRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetailInvoiceRoom() throws Exception {
        int databaseSizeBeforeUpdate = detailInvoiceRoomRepository.findAll().size();
        detailInvoiceRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailInvoiceRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailInvoiceRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailInvoiceRoom in the database
        List<DetailInvoiceRoom> detailInvoiceRoomList = detailInvoiceRoomRepository.findAll();
        assertThat(detailInvoiceRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetailInvoiceRoom() throws Exception {
        int databaseSizeBeforeUpdate = detailInvoiceRoomRepository.findAll().size();
        detailInvoiceRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailInvoiceRoomMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailInvoiceRoom))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailInvoiceRoom in the database
        List<DetailInvoiceRoom> detailInvoiceRoomList = detailInvoiceRoomRepository.findAll();
        assertThat(detailInvoiceRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetailInvoiceRoomWithPatch() throws Exception {
        // Initialize the database
        detailInvoiceRoomRepository.saveAndFlush(detailInvoiceRoom);

        int databaseSizeBeforeUpdate = detailInvoiceRoomRepository.findAll().size();

        // Update the detailInvoiceRoom using partial update
        DetailInvoiceRoom partialUpdatedDetailInvoiceRoom = new DetailInvoiceRoom();
        partialUpdatedDetailInvoiceRoom.setId(detailInvoiceRoom.getId());

        partialUpdatedDetailInvoiceRoom.price(UPDATED_PRICE);

        restDetailInvoiceRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailInvoiceRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailInvoiceRoom))
            )
            .andExpect(status().isOk());

        // Validate the DetailInvoiceRoom in the database
        List<DetailInvoiceRoom> detailInvoiceRoomList = detailInvoiceRoomRepository.findAll();
        assertThat(detailInvoiceRoomList).hasSize(databaseSizeBeforeUpdate);
        DetailInvoiceRoom testDetailInvoiceRoom = detailInvoiceRoomList.get(detailInvoiceRoomList.size() - 1);
        assertThat(testDetailInvoiceRoom.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateDetailInvoiceRoomWithPatch() throws Exception {
        // Initialize the database
        detailInvoiceRoomRepository.saveAndFlush(detailInvoiceRoom);

        int databaseSizeBeforeUpdate = detailInvoiceRoomRepository.findAll().size();

        // Update the detailInvoiceRoom using partial update
        DetailInvoiceRoom partialUpdatedDetailInvoiceRoom = new DetailInvoiceRoom();
        partialUpdatedDetailInvoiceRoom.setId(detailInvoiceRoom.getId());

        partialUpdatedDetailInvoiceRoom.price(UPDATED_PRICE);

        restDetailInvoiceRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailInvoiceRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailInvoiceRoom))
            )
            .andExpect(status().isOk());

        // Validate the DetailInvoiceRoom in the database
        List<DetailInvoiceRoom> detailInvoiceRoomList = detailInvoiceRoomRepository.findAll();
        assertThat(detailInvoiceRoomList).hasSize(databaseSizeBeforeUpdate);
        DetailInvoiceRoom testDetailInvoiceRoom = detailInvoiceRoomList.get(detailInvoiceRoomList.size() - 1);
        assertThat(testDetailInvoiceRoom.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingDetailInvoiceRoom() throws Exception {
        int databaseSizeBeforeUpdate = detailInvoiceRoomRepository.findAll().size();
        detailInvoiceRoom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailInvoiceRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detailInvoiceRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailInvoiceRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailInvoiceRoom in the database
        List<DetailInvoiceRoom> detailInvoiceRoomList = detailInvoiceRoomRepository.findAll();
        assertThat(detailInvoiceRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetailInvoiceRoom() throws Exception {
        int databaseSizeBeforeUpdate = detailInvoiceRoomRepository.findAll().size();
        detailInvoiceRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailInvoiceRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailInvoiceRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailInvoiceRoom in the database
        List<DetailInvoiceRoom> detailInvoiceRoomList = detailInvoiceRoomRepository.findAll();
        assertThat(detailInvoiceRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetailInvoiceRoom() throws Exception {
        int databaseSizeBeforeUpdate = detailInvoiceRoomRepository.findAll().size();
        detailInvoiceRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailInvoiceRoomMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailInvoiceRoom))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailInvoiceRoom in the database
        List<DetailInvoiceRoom> detailInvoiceRoomList = detailInvoiceRoomRepository.findAll();
        assertThat(detailInvoiceRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetailInvoiceRoom() throws Exception {
        // Initialize the database
        detailInvoiceRoomRepository.saveAndFlush(detailInvoiceRoom);

        int databaseSizeBeforeDelete = detailInvoiceRoomRepository.findAll().size();

        // Delete the detailInvoiceRoom
        restDetailInvoiceRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, detailInvoiceRoom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DetailInvoiceRoom> detailInvoiceRoomList = detailInvoiceRoomRepository.findAll();
        assertThat(detailInvoiceRoomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
