package com.booking.tai.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.booking.tai.IntegrationTest;
import com.booking.tai.domain.PriceRoom;
import com.booking.tai.repository.PriceRoomRepository;
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
 * Integration tests for the {@link PriceRoomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PriceRoomResourceIT {

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/price-rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PriceRoomRepository priceRoomRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPriceRoomMockMvc;

    private PriceRoom priceRoom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceRoom createEntity(EntityManager em) {
        PriceRoom priceRoom = new PriceRoom().price(DEFAULT_PRICE).unit(DEFAULT_UNIT);
        return priceRoom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PriceRoom createUpdatedEntity(EntityManager em) {
        PriceRoom priceRoom = new PriceRoom().price(UPDATED_PRICE).unit(UPDATED_UNIT);
        return priceRoom;
    }

    @BeforeEach
    public void initTest() {
        priceRoom = createEntity(em);
    }

    @Test
    @Transactional
    void createPriceRoom() throws Exception {
        int databaseSizeBeforeCreate = priceRoomRepository.findAll().size();
        // Create the PriceRoom
        restPriceRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priceRoom)))
            .andExpect(status().isCreated());

        // Validate the PriceRoom in the database
        List<PriceRoom> priceRoomList = priceRoomRepository.findAll();
        assertThat(priceRoomList).hasSize(databaseSizeBeforeCreate + 1);
        PriceRoom testPriceRoom = priceRoomList.get(priceRoomList.size() - 1);
        assertThat(testPriceRoom.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testPriceRoom.getUnit()).isEqualTo(DEFAULT_UNIT);
    }

    @Test
    @Transactional
    void createPriceRoomWithExistingId() throws Exception {
        // Create the PriceRoom with an existing ID
        priceRoom.setId(1L);

        int databaseSizeBeforeCreate = priceRoomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPriceRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priceRoom)))
            .andExpect(status().isBadRequest());

        // Validate the PriceRoom in the database
        List<PriceRoom> priceRoomList = priceRoomRepository.findAll();
        assertThat(priceRoomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPriceRooms() throws Exception {
        // Initialize the database
        priceRoomRepository.saveAndFlush(priceRoom);

        // Get all the priceRoomList
        restPriceRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(priceRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)));
    }

    @Test
    @Transactional
    void getPriceRoom() throws Exception {
        // Initialize the database
        priceRoomRepository.saveAndFlush(priceRoom);

        // Get the priceRoom
        restPriceRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, priceRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(priceRoom.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT));
    }

    @Test
    @Transactional
    void getNonExistingPriceRoom() throws Exception {
        // Get the priceRoom
        restPriceRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPriceRoom() throws Exception {
        // Initialize the database
        priceRoomRepository.saveAndFlush(priceRoom);

        int databaseSizeBeforeUpdate = priceRoomRepository.findAll().size();

        // Update the priceRoom
        PriceRoom updatedPriceRoom = priceRoomRepository.findById(priceRoom.getId()).get();
        // Disconnect from session so that the updates on updatedPriceRoom are not directly saved in db
        em.detach(updatedPriceRoom);
        updatedPriceRoom.price(UPDATED_PRICE).unit(UPDATED_UNIT);

        restPriceRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPriceRoom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPriceRoom))
            )
            .andExpect(status().isOk());

        // Validate the PriceRoom in the database
        List<PriceRoom> priceRoomList = priceRoomRepository.findAll();
        assertThat(priceRoomList).hasSize(databaseSizeBeforeUpdate);
        PriceRoom testPriceRoom = priceRoomList.get(priceRoomList.size() - 1);
        assertThat(testPriceRoom.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testPriceRoom.getUnit()).isEqualTo(UPDATED_UNIT);
    }

    @Test
    @Transactional
    void putNonExistingPriceRoom() throws Exception {
        int databaseSizeBeforeUpdate = priceRoomRepository.findAll().size();
        priceRoom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, priceRoom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceRoom in the database
        List<PriceRoom> priceRoomList = priceRoomRepository.findAll();
        assertThat(priceRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPriceRoom() throws Exception {
        int databaseSizeBeforeUpdate = priceRoomRepository.findAll().size();
        priceRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(priceRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceRoom in the database
        List<PriceRoom> priceRoomList = priceRoomRepository.findAll();
        assertThat(priceRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPriceRoom() throws Exception {
        int databaseSizeBeforeUpdate = priceRoomRepository.findAll().size();
        priceRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceRoomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(priceRoom)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriceRoom in the database
        List<PriceRoom> priceRoomList = priceRoomRepository.findAll();
        assertThat(priceRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePriceRoomWithPatch() throws Exception {
        // Initialize the database
        priceRoomRepository.saveAndFlush(priceRoom);

        int databaseSizeBeforeUpdate = priceRoomRepository.findAll().size();

        // Update the priceRoom using partial update
        PriceRoom partialUpdatedPriceRoom = new PriceRoom();
        partialUpdatedPriceRoom.setId(priceRoom.getId());

        partialUpdatedPriceRoom.unit(UPDATED_UNIT);

        restPriceRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriceRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriceRoom))
            )
            .andExpect(status().isOk());

        // Validate the PriceRoom in the database
        List<PriceRoom> priceRoomList = priceRoomRepository.findAll();
        assertThat(priceRoomList).hasSize(databaseSizeBeforeUpdate);
        PriceRoom testPriceRoom = priceRoomList.get(priceRoomList.size() - 1);
        assertThat(testPriceRoom.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testPriceRoom.getUnit()).isEqualTo(UPDATED_UNIT);
    }

    @Test
    @Transactional
    void fullUpdatePriceRoomWithPatch() throws Exception {
        // Initialize the database
        priceRoomRepository.saveAndFlush(priceRoom);

        int databaseSizeBeforeUpdate = priceRoomRepository.findAll().size();

        // Update the priceRoom using partial update
        PriceRoom partialUpdatedPriceRoom = new PriceRoom();
        partialUpdatedPriceRoom.setId(priceRoom.getId());

        partialUpdatedPriceRoom.price(UPDATED_PRICE).unit(UPDATED_UNIT);

        restPriceRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPriceRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPriceRoom))
            )
            .andExpect(status().isOk());

        // Validate the PriceRoom in the database
        List<PriceRoom> priceRoomList = priceRoomRepository.findAll();
        assertThat(priceRoomList).hasSize(databaseSizeBeforeUpdate);
        PriceRoom testPriceRoom = priceRoomList.get(priceRoomList.size() - 1);
        assertThat(testPriceRoom.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testPriceRoom.getUnit()).isEqualTo(UPDATED_UNIT);
    }

    @Test
    @Transactional
    void patchNonExistingPriceRoom() throws Exception {
        int databaseSizeBeforeUpdate = priceRoomRepository.findAll().size();
        priceRoom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPriceRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, priceRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceRoom in the database
        List<PriceRoom> priceRoomList = priceRoomRepository.findAll();
        assertThat(priceRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPriceRoom() throws Exception {
        int databaseSizeBeforeUpdate = priceRoomRepository.findAll().size();
        priceRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(priceRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the PriceRoom in the database
        List<PriceRoom> priceRoomList = priceRoomRepository.findAll();
        assertThat(priceRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPriceRoom() throws Exception {
        int databaseSizeBeforeUpdate = priceRoomRepository.findAll().size();
        priceRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPriceRoomMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(priceRoom))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PriceRoom in the database
        List<PriceRoom> priceRoomList = priceRoomRepository.findAll();
        assertThat(priceRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePriceRoom() throws Exception {
        // Initialize the database
        priceRoomRepository.saveAndFlush(priceRoom);

        int databaseSizeBeforeDelete = priceRoomRepository.findAll().size();

        // Delete the priceRoom
        restPriceRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, priceRoom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PriceRoom> priceRoomList = priceRoomRepository.findAll();
        assertThat(priceRoomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
