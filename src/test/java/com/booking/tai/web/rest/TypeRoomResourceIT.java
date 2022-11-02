package com.booking.tai.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.booking.tai.IntegrationTest;
import com.booking.tai.domain.TypeRoom;
import com.booking.tai.repository.TypeRoomRepository;
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
 * Integration tests for the {@link TypeRoomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TypeRoomResourceIT {

    private static final String DEFAULT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TYPE_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/type-rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TypeRoomRepository typeRoomRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTypeRoomMockMvc;

    private TypeRoom typeRoom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeRoom createEntity(EntityManager em) {
        TypeRoom typeRoom = new TypeRoom().typeName(DEFAULT_TYPE_NAME);
        return typeRoom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TypeRoom createUpdatedEntity(EntityManager em) {
        TypeRoom typeRoom = new TypeRoom().typeName(UPDATED_TYPE_NAME);
        return typeRoom;
    }

    @BeforeEach
    public void initTest() {
        typeRoom = createEntity(em);
    }

    @Test
    @Transactional
    void createTypeRoom() throws Exception {
        int databaseSizeBeforeCreate = typeRoomRepository.findAll().size();
        // Create the TypeRoom
        restTypeRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeRoom)))
            .andExpect(status().isCreated());

        // Validate the TypeRoom in the database
        List<TypeRoom> typeRoomList = typeRoomRepository.findAll();
        assertThat(typeRoomList).hasSize(databaseSizeBeforeCreate + 1);
        TypeRoom testTypeRoom = typeRoomList.get(typeRoomList.size() - 1);
        assertThat(testTypeRoom.getTypeName()).isEqualTo(DEFAULT_TYPE_NAME);
    }

    @Test
    @Transactional
    void createTypeRoomWithExistingId() throws Exception {
        // Create the TypeRoom with an existing ID
        typeRoom.setId(1L);

        int databaseSizeBeforeCreate = typeRoomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTypeRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeRoom)))
            .andExpect(status().isBadRequest());

        // Validate the TypeRoom in the database
        List<TypeRoom> typeRoomList = typeRoomRepository.findAll();
        assertThat(typeRoomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTypeRooms() throws Exception {
        // Initialize the database
        typeRoomRepository.saveAndFlush(typeRoom);

        // Get all the typeRoomList
        restTypeRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(typeRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].typeName").value(hasItem(DEFAULT_TYPE_NAME)));
    }

    @Test
    @Transactional
    void getTypeRoom() throws Exception {
        // Initialize the database
        typeRoomRepository.saveAndFlush(typeRoom);

        // Get the typeRoom
        restTypeRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, typeRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(typeRoom.getId().intValue()))
            .andExpect(jsonPath("$.typeName").value(DEFAULT_TYPE_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTypeRoom() throws Exception {
        // Get the typeRoom
        restTypeRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTypeRoom() throws Exception {
        // Initialize the database
        typeRoomRepository.saveAndFlush(typeRoom);

        int databaseSizeBeforeUpdate = typeRoomRepository.findAll().size();

        // Update the typeRoom
        TypeRoom updatedTypeRoom = typeRoomRepository.findById(typeRoom.getId()).get();
        // Disconnect from session so that the updates on updatedTypeRoom are not directly saved in db
        em.detach(updatedTypeRoom);
        updatedTypeRoom.typeName(UPDATED_TYPE_NAME);

        restTypeRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTypeRoom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTypeRoom))
            )
            .andExpect(status().isOk());

        // Validate the TypeRoom in the database
        List<TypeRoom> typeRoomList = typeRoomRepository.findAll();
        assertThat(typeRoomList).hasSize(databaseSizeBeforeUpdate);
        TypeRoom testTypeRoom = typeRoomList.get(typeRoomList.size() - 1);
        assertThat(testTypeRoom.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTypeRoom() throws Exception {
        int databaseSizeBeforeUpdate = typeRoomRepository.findAll().size();
        typeRoom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, typeRoom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRoom in the database
        List<TypeRoom> typeRoomList = typeRoomRepository.findAll();
        assertThat(typeRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTypeRoom() throws Exception {
        int databaseSizeBeforeUpdate = typeRoomRepository.findAll().size();
        typeRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(typeRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRoom in the database
        List<TypeRoom> typeRoomList = typeRoomRepository.findAll();
        assertThat(typeRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTypeRoom() throws Exception {
        int databaseSizeBeforeUpdate = typeRoomRepository.findAll().size();
        typeRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRoomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(typeRoom)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeRoom in the database
        List<TypeRoom> typeRoomList = typeRoomRepository.findAll();
        assertThat(typeRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTypeRoomWithPatch() throws Exception {
        // Initialize the database
        typeRoomRepository.saveAndFlush(typeRoom);

        int databaseSizeBeforeUpdate = typeRoomRepository.findAll().size();

        // Update the typeRoom using partial update
        TypeRoom partialUpdatedTypeRoom = new TypeRoom();
        partialUpdatedTypeRoom.setId(typeRoom.getId());

        partialUpdatedTypeRoom.typeName(UPDATED_TYPE_NAME);

        restTypeRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeRoom))
            )
            .andExpect(status().isOk());

        // Validate the TypeRoom in the database
        List<TypeRoom> typeRoomList = typeRoomRepository.findAll();
        assertThat(typeRoomList).hasSize(databaseSizeBeforeUpdate);
        TypeRoom testTypeRoom = typeRoomList.get(typeRoomList.size() - 1);
        assertThat(testTypeRoom.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTypeRoomWithPatch() throws Exception {
        // Initialize the database
        typeRoomRepository.saveAndFlush(typeRoom);

        int databaseSizeBeforeUpdate = typeRoomRepository.findAll().size();

        // Update the typeRoom using partial update
        TypeRoom partialUpdatedTypeRoom = new TypeRoom();
        partialUpdatedTypeRoom.setId(typeRoom.getId());

        partialUpdatedTypeRoom.typeName(UPDATED_TYPE_NAME);

        restTypeRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTypeRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTypeRoom))
            )
            .andExpect(status().isOk());

        // Validate the TypeRoom in the database
        List<TypeRoom> typeRoomList = typeRoomRepository.findAll();
        assertThat(typeRoomList).hasSize(databaseSizeBeforeUpdate);
        TypeRoom testTypeRoom = typeRoomList.get(typeRoomList.size() - 1);
        assertThat(testTypeRoom.getTypeName()).isEqualTo(UPDATED_TYPE_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTypeRoom() throws Exception {
        int databaseSizeBeforeUpdate = typeRoomRepository.findAll().size();
        typeRoom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTypeRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, typeRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRoom in the database
        List<TypeRoom> typeRoomList = typeRoomRepository.findAll();
        assertThat(typeRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTypeRoom() throws Exception {
        int databaseSizeBeforeUpdate = typeRoomRepository.findAll().size();
        typeRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(typeRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the TypeRoom in the database
        List<TypeRoom> typeRoomList = typeRoomRepository.findAll();
        assertThat(typeRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTypeRoom() throws Exception {
        int databaseSizeBeforeUpdate = typeRoomRepository.findAll().size();
        typeRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTypeRoomMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(typeRoom)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TypeRoom in the database
        List<TypeRoom> typeRoomList = typeRoomRepository.findAll();
        assertThat(typeRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTypeRoom() throws Exception {
        // Initialize the database
        typeRoomRepository.saveAndFlush(typeRoom);

        int databaseSizeBeforeDelete = typeRoomRepository.findAll().size();

        // Delete the typeRoom
        restTypeRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, typeRoom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TypeRoom> typeRoomList = typeRoomRepository.findAll();
        assertThat(typeRoomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
