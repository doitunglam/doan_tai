package com.booking.tai.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.booking.tai.IntegrationTest;
import com.booking.tai.domain.ImageServices;
import com.booking.tai.repository.ImageServicesRepository;
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
 * Integration tests for the {@link ImageServicesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImageServicesResourceIT {

    private static final String DEFAULT_LINKIMAGE = "AAAAAAAAAA";
    private static final String UPDATED_LINKIMAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/image-services";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ImageServicesRepository imageServicesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageServicesMockMvc;

    private ImageServices imageServices;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageServices createEntity(EntityManager em) {
        ImageServices imageServices = new ImageServices().linkimage(DEFAULT_LINKIMAGE);
        return imageServices;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageServices createUpdatedEntity(EntityManager em) {
        ImageServices imageServices = new ImageServices().linkimage(UPDATED_LINKIMAGE);
        return imageServices;
    }

    @BeforeEach
    public void initTest() {
        imageServices = createEntity(em);
    }

    @Test
    @Transactional
    void createImageServices() throws Exception {
        int databaseSizeBeforeCreate = imageServicesRepository.findAll().size();
        // Create the ImageServices
        restImageServicesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageServices)))
            .andExpect(status().isCreated());

        // Validate the ImageServices in the database
        List<ImageServices> imageServicesList = imageServicesRepository.findAll();
        assertThat(imageServicesList).hasSize(databaseSizeBeforeCreate + 1);
        ImageServices testImageServices = imageServicesList.get(imageServicesList.size() - 1);
        assertThat(testImageServices.getLinkimage()).isEqualTo(DEFAULT_LINKIMAGE);
    }

    @Test
    @Transactional
    void createImageServicesWithExistingId() throws Exception {
        // Create the ImageServices with an existing ID
        imageServices.setId(1L);

        int databaseSizeBeforeCreate = imageServicesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageServicesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageServices)))
            .andExpect(status().isBadRequest());

        // Validate the ImageServices in the database
        List<ImageServices> imageServicesList = imageServicesRepository.findAll();
        assertThat(imageServicesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllImageServices() throws Exception {
        // Initialize the database
        imageServicesRepository.saveAndFlush(imageServices);

        // Get all the imageServicesList
        restImageServicesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageServices.getId().intValue())))
            .andExpect(jsonPath("$.[*].linkimage").value(hasItem(DEFAULT_LINKIMAGE)));
    }

    @Test
    @Transactional
    void getImageServices() throws Exception {
        // Initialize the database
        imageServicesRepository.saveAndFlush(imageServices);

        // Get the imageServices
        restImageServicesMockMvc
            .perform(get(ENTITY_API_URL_ID, imageServices.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imageServices.getId().intValue()))
            .andExpect(jsonPath("$.linkimage").value(DEFAULT_LINKIMAGE));
    }

    @Test
    @Transactional
    void getNonExistingImageServices() throws Exception {
        // Get the imageServices
        restImageServicesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewImageServices() throws Exception {
        // Initialize the database
        imageServicesRepository.saveAndFlush(imageServices);

        int databaseSizeBeforeUpdate = imageServicesRepository.findAll().size();

        // Update the imageServices
        ImageServices updatedImageServices = imageServicesRepository.findById(imageServices.getId()).get();
        // Disconnect from session so that the updates on updatedImageServices are not directly saved in db
        em.detach(updatedImageServices);
        updatedImageServices.linkimage(UPDATED_LINKIMAGE);

        restImageServicesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedImageServices.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedImageServices))
            )
            .andExpect(status().isOk());

        // Validate the ImageServices in the database
        List<ImageServices> imageServicesList = imageServicesRepository.findAll();
        assertThat(imageServicesList).hasSize(databaseSizeBeforeUpdate);
        ImageServices testImageServices = imageServicesList.get(imageServicesList.size() - 1);
        assertThat(testImageServices.getLinkimage()).isEqualTo(UPDATED_LINKIMAGE);
    }

    @Test
    @Transactional
    void putNonExistingImageServices() throws Exception {
        int databaseSizeBeforeUpdate = imageServicesRepository.findAll().size();
        imageServices.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageServicesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageServices.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageServices))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageServices in the database
        List<ImageServices> imageServicesList = imageServicesRepository.findAll();
        assertThat(imageServicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImageServices() throws Exception {
        int databaseSizeBeforeUpdate = imageServicesRepository.findAll().size();
        imageServices.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageServicesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(imageServices))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageServices in the database
        List<ImageServices> imageServicesList = imageServicesRepository.findAll();
        assertThat(imageServicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImageServices() throws Exception {
        int databaseSizeBeforeUpdate = imageServicesRepository.findAll().size();
        imageServices.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageServicesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(imageServices)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageServices in the database
        List<ImageServices> imageServicesList = imageServicesRepository.findAll();
        assertThat(imageServicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImageServicesWithPatch() throws Exception {
        // Initialize the database
        imageServicesRepository.saveAndFlush(imageServices);

        int databaseSizeBeforeUpdate = imageServicesRepository.findAll().size();

        // Update the imageServices using partial update
        ImageServices partialUpdatedImageServices = new ImageServices();
        partialUpdatedImageServices.setId(imageServices.getId());

        partialUpdatedImageServices.linkimage(UPDATED_LINKIMAGE);

        restImageServicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageServices.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImageServices))
            )
            .andExpect(status().isOk());

        // Validate the ImageServices in the database
        List<ImageServices> imageServicesList = imageServicesRepository.findAll();
        assertThat(imageServicesList).hasSize(databaseSizeBeforeUpdate);
        ImageServices testImageServices = imageServicesList.get(imageServicesList.size() - 1);
        assertThat(testImageServices.getLinkimage()).isEqualTo(UPDATED_LINKIMAGE);
    }

    @Test
    @Transactional
    void fullUpdateImageServicesWithPatch() throws Exception {
        // Initialize the database
        imageServicesRepository.saveAndFlush(imageServices);

        int databaseSizeBeforeUpdate = imageServicesRepository.findAll().size();

        // Update the imageServices using partial update
        ImageServices partialUpdatedImageServices = new ImageServices();
        partialUpdatedImageServices.setId(imageServices.getId());

        partialUpdatedImageServices.linkimage(UPDATED_LINKIMAGE);

        restImageServicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageServices.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedImageServices))
            )
            .andExpect(status().isOk());

        // Validate the ImageServices in the database
        List<ImageServices> imageServicesList = imageServicesRepository.findAll();
        assertThat(imageServicesList).hasSize(databaseSizeBeforeUpdate);
        ImageServices testImageServices = imageServicesList.get(imageServicesList.size() - 1);
        assertThat(testImageServices.getLinkimage()).isEqualTo(UPDATED_LINKIMAGE);
    }

    @Test
    @Transactional
    void patchNonExistingImageServices() throws Exception {
        int databaseSizeBeforeUpdate = imageServicesRepository.findAll().size();
        imageServices.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageServicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imageServices.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageServices))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageServices in the database
        List<ImageServices> imageServicesList = imageServicesRepository.findAll();
        assertThat(imageServicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImageServices() throws Exception {
        int databaseSizeBeforeUpdate = imageServicesRepository.findAll().size();
        imageServices.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageServicesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(imageServices))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageServices in the database
        List<ImageServices> imageServicesList = imageServicesRepository.findAll();
        assertThat(imageServicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImageServices() throws Exception {
        int databaseSizeBeforeUpdate = imageServicesRepository.findAll().size();
        imageServices.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageServicesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(imageServices))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageServices in the database
        List<ImageServices> imageServicesList = imageServicesRepository.findAll();
        assertThat(imageServicesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImageServices() throws Exception {
        // Initialize the database
        imageServicesRepository.saveAndFlush(imageServices);

        int databaseSizeBeforeDelete = imageServicesRepository.findAll().size();

        // Delete the imageServices
        restImageServicesMockMvc
            .perform(delete(ENTITY_API_URL_ID, imageServices.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ImageServices> imageServicesList = imageServicesRepository.findAll();
        assertThat(imageServicesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
