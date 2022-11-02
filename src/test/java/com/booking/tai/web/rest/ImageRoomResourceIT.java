package com.booking.tai.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.booking.tai.IntegrationTest;
import com.booking.tai.domain.ImageRoom;
import com.booking.tai.repository.ImageRoomRepository;
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
 * Integration tests for the {@link ImageRoomResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImageRoomResourceIT {

    private static final String DEFAULT_LINK_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_LINK_IMAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/image-rooms";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImageRoomRepository imageRoomRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageRoomMockMvc;

    private ImageRoom imageRoom;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageRoom createEntity(EntityManager em) {
        ImageRoom imageRoom = new ImageRoom().linkImage(DEFAULT_LINK_IMAGE);
        return imageRoom;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageRoom createUpdatedEntity(EntityManager em) {
        ImageRoom imageRoom = new ImageRoom().linkImage(UPDATED_LINK_IMAGE);
        return imageRoom;
    }

    @BeforeEach
    public void initTest() {
        imageRoom = createEntity(em);
    }

    @Test
    @Transactional
    void createImageRoom() throws Exception {
        int databaseSizeBeforeCreate = imageRoomRepository.findAll().size();
        // Create the ImageRoom
        restImageRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageRoom)))
            .andExpect(status().isCreated());

        // Validate the ImageRoom in the database
        List<ImageRoom> imageRoomList = imageRoomRepository.findAll();
        assertThat(imageRoomList).hasSize(databaseSizeBeforeCreate + 1);
        ImageRoom testImageRoom = imageRoomList.get(imageRoomList.size() - 1);
        assertThat(testImageRoom.getLinkImage()).isEqualTo(DEFAULT_LINK_IMAGE);
    }

    @Test
    @Transactional
    void createImageRoomWithExistingId() throws Exception {
        // Create the ImageRoom with an existing ID
        imageRoom.setId(1L);

        int databaseSizeBeforeCreate = imageRoomRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageRoomMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageRoom)))
            .andExpect(status().isBadRequest());

        // Validate the ImageRoom in the database
        List<ImageRoom> imageRoomList = imageRoomRepository.findAll();
        assertThat(imageRoomList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllImageRooms() throws Exception {
        // Initialize the database
        imageRoomRepository.saveAndFlush(imageRoom);

        // Get all the imageRoomList
        restImageRoomMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageRoom.getId().intValue())))
            .andExpect(jsonPath("$.[*].linkImage").value(hasItem(DEFAULT_LINK_IMAGE)));
    }

    @Test
    @Transactional
    void getImageRoom() throws Exception {
        // Initialize the database
        imageRoomRepository.saveAndFlush(imageRoom);

        // Get the imageRoom
        restImageRoomMockMvc
            .perform(get(ENTITY_API_URL_ID, imageRoom.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imageRoom.getId().intValue()))
            .andExpect(jsonPath("$.linkImage").value(DEFAULT_LINK_IMAGE));
    }

    @Test
    @Transactional
    void getNonExistingImageRoom() throws Exception {
        // Get the imageRoom
        restImageRoomMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewImageRoom() throws Exception {
        // Initialize the database
        imageRoomRepository.saveAndFlush(imageRoom);

        int databaseSizeBeforeUpdate = imageRoomRepository.findAll().size();

        // Update the imageRoom
        ImageRoom updatedImageRoom = imageRoomRepository.findById(imageRoom.getId()).get();
        // Disconnect from session so that the updates on updatedImageRoom are not directly saved in db
        em.detach(updatedImageRoom);
        updatedImageRoom.linkImage(UPDATED_LINK_IMAGE);

        restImageRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedImageRoom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedImageRoom))
            )
            .andExpect(status().isOk());

        // Validate the ImageRoom in the database
        List<ImageRoom> imageRoomList = imageRoomRepository.findAll();
        assertThat(imageRoomList).hasSize(databaseSizeBeforeUpdate);
        ImageRoom testImageRoom = imageRoomList.get(imageRoomList.size() - 1);
        assertThat(testImageRoom.getLinkImage()).isEqualTo(UPDATED_LINK_IMAGE);
    }

    @Test
    @Transactional
    void putNonExistingImageRoom() throws Exception {
        int databaseSizeBeforeUpdate = imageRoomRepository.findAll().size();
        imageRoom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageRoom.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageRoom in the database
        List<ImageRoom> imageRoomList = imageRoomRepository.findAll();
        assertThat(imageRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImageRoom() throws Exception {
        int databaseSizeBeforeUpdate = imageRoomRepository.findAll().size();
        imageRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageRoomMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageRoom in the database
        List<ImageRoom> imageRoomList = imageRoomRepository.findAll();
        assertThat(imageRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImageRoom() throws Exception {
        int databaseSizeBeforeUpdate = imageRoomRepository.findAll().size();
        imageRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageRoomMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageRoom)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageRoom in the database
        List<ImageRoom> imageRoomList = imageRoomRepository.findAll();
        assertThat(imageRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImageRoomWithPatch() throws Exception {
        // Initialize the database
        imageRoomRepository.saveAndFlush(imageRoom);

        int databaseSizeBeforeUpdate = imageRoomRepository.findAll().size();

        // Update the imageRoom using partial update
        ImageRoom partialUpdatedImageRoom = new ImageRoom();
        partialUpdatedImageRoom.setId(imageRoom.getId());

        partialUpdatedImageRoom.linkImage(UPDATED_LINK_IMAGE);

        restImageRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImageRoom))
            )
            .andExpect(status().isOk());

        // Validate the ImageRoom in the database
        List<ImageRoom> imageRoomList = imageRoomRepository.findAll();
        assertThat(imageRoomList).hasSize(databaseSizeBeforeUpdate);
        ImageRoom testImageRoom = imageRoomList.get(imageRoomList.size() - 1);
        assertThat(testImageRoom.getLinkImage()).isEqualTo(UPDATED_LINK_IMAGE);
    }

    @Test
    @Transactional
    void fullUpdateImageRoomWithPatch() throws Exception {
        // Initialize the database
        imageRoomRepository.saveAndFlush(imageRoom);

        int databaseSizeBeforeUpdate = imageRoomRepository.findAll().size();

        // Update the imageRoom using partial update
        ImageRoom partialUpdatedImageRoom = new ImageRoom();
        partialUpdatedImageRoom.setId(imageRoom.getId());

        partialUpdatedImageRoom.linkImage(UPDATED_LINK_IMAGE);

        restImageRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImageRoom))
            )
            .andExpect(status().isOk());

        // Validate the ImageRoom in the database
        List<ImageRoom> imageRoomList = imageRoomRepository.findAll();
        assertThat(imageRoomList).hasSize(databaseSizeBeforeUpdate);
        ImageRoom testImageRoom = imageRoomList.get(imageRoomList.size() - 1);
        assertThat(testImageRoom.getLinkImage()).isEqualTo(UPDATED_LINK_IMAGE);
    }

    @Test
    @Transactional
    void patchNonExistingImageRoom() throws Exception {
        int databaseSizeBeforeUpdate = imageRoomRepository.findAll().size();
        imageRoom.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imageRoom.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageRoom in the database
        List<ImageRoom> imageRoomList = imageRoomRepository.findAll();
        assertThat(imageRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImageRoom() throws Exception {
        int databaseSizeBeforeUpdate = imageRoomRepository.findAll().size();
        imageRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageRoomMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageRoom))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageRoom in the database
        List<ImageRoom> imageRoomList = imageRoomRepository.findAll();
        assertThat(imageRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImageRoom() throws Exception {
        int databaseSizeBeforeUpdate = imageRoomRepository.findAll().size();
        imageRoom.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageRoomMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(imageRoom))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageRoom in the database
        List<ImageRoom> imageRoomList = imageRoomRepository.findAll();
        assertThat(imageRoomList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImageRoom() throws Exception {
        // Initialize the database
        imageRoomRepository.saveAndFlush(imageRoom);

        int databaseSizeBeforeDelete = imageRoomRepository.findAll().size();

        // Delete the imageRoom
        restImageRoomMockMvc
            .perform(delete(ENTITY_API_URL_ID, imageRoom.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ImageRoom> imageRoomList = imageRoomRepository.findAll();
        assertThat(imageRoomList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
