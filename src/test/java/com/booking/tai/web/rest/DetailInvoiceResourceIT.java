package com.booking.tai.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.booking.tai.IntegrationTest;
import com.booking.tai.domain.DetailInvoice;
import com.booking.tai.repository.DetailInvoiceRepository;
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
 * Integration tests for the {@link DetailInvoiceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DetailInvoiceResourceIT {

    private static final Float DEFAULT_QUANTITY = 1F;
    private static final Float UPDATED_QUANTITY = 2F;

    private static final Double DEFAULT_PRICE = 1D;
    private static final Double UPDATED_PRICE = 2D;

    private static final String ENTITY_API_URL = "/api/detail-invoices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DetailInvoiceRepository detailInvoiceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDetailInvoiceMockMvc;

    private DetailInvoice detailInvoice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailInvoice createEntity(EntityManager em) {
        DetailInvoice detailInvoice = new DetailInvoice().quantity(DEFAULT_QUANTITY).price(DEFAULT_PRICE);
        return detailInvoice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DetailInvoice createUpdatedEntity(EntityManager em) {
        DetailInvoice detailInvoice = new DetailInvoice().quantity(UPDATED_QUANTITY).price(UPDATED_PRICE);
        return detailInvoice;
    }

    @BeforeEach
    public void initTest() {
        detailInvoice = createEntity(em);
    }

    @Test
    @Transactional
    void createDetailInvoice() throws Exception {
        int databaseSizeBeforeCreate = detailInvoiceRepository.findAll().size();
        // Create the DetailInvoice
        restDetailInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailInvoice)))
            .andExpect(status().isCreated());

        // Validate the DetailInvoice in the database
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeCreate + 1);
        DetailInvoice testDetailInvoice = detailInvoiceList.get(detailInvoiceList.size() - 1);
        assertThat(testDetailInvoice.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testDetailInvoice.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void createDetailInvoiceWithExistingId() throws Exception {
        // Create the DetailInvoice with an existing ID
        detailInvoice.setId(1L);

        int databaseSizeBeforeCreate = detailInvoiceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDetailInvoiceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailInvoice)))
            .andExpect(status().isBadRequest());

        // Validate the DetailInvoice in the database
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDetailInvoices() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get all the detailInvoiceList
        restDetailInvoiceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(detailInvoice.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())));
    }

    @Test
    @Transactional
    void getDetailInvoice() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        // Get the detailInvoice
        restDetailInvoiceMockMvc
            .perform(get(ENTITY_API_URL_ID, detailInvoice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(detailInvoice.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.doubleValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingDetailInvoice() throws Exception {
        // Get the detailInvoice
        restDetailInvoiceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDetailInvoice() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        int databaseSizeBeforeUpdate = detailInvoiceRepository.findAll().size();

        // Update the detailInvoice
        DetailInvoice updatedDetailInvoice = detailInvoiceRepository.findById(detailInvoice.getId()).get();
        // Disconnect from session so that the updates on updatedDetailInvoice are not directly saved in db
        em.detach(updatedDetailInvoice);
        updatedDetailInvoice.quantity(UPDATED_QUANTITY).price(UPDATED_PRICE);

        restDetailInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDetailInvoice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedDetailInvoice))
            )
            .andExpect(status().isOk());

        // Validate the DetailInvoice in the database
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeUpdate);
        DetailInvoice testDetailInvoice = detailInvoiceList.get(detailInvoiceList.size() - 1);
        assertThat(testDetailInvoice.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testDetailInvoice.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingDetailInvoice() throws Exception {
        int databaseSizeBeforeUpdate = detailInvoiceRepository.findAll().size();
        detailInvoice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, detailInvoice.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailInvoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailInvoice in the database
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDetailInvoice() throws Exception {
        int databaseSizeBeforeUpdate = detailInvoiceRepository.findAll().size();
        detailInvoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailInvoiceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(detailInvoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailInvoice in the database
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDetailInvoice() throws Exception {
        int databaseSizeBeforeUpdate = detailInvoiceRepository.findAll().size();
        detailInvoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailInvoiceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(detailInvoice)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailInvoice in the database
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDetailInvoiceWithPatch() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        int databaseSizeBeforeUpdate = detailInvoiceRepository.findAll().size();

        // Update the detailInvoice using partial update
        DetailInvoice partialUpdatedDetailInvoice = new DetailInvoice();
        partialUpdatedDetailInvoice.setId(detailInvoice.getId());

        restDetailInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailInvoice))
            )
            .andExpect(status().isOk());

        // Validate the DetailInvoice in the database
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeUpdate);
        DetailInvoice testDetailInvoice = detailInvoiceList.get(detailInvoiceList.size() - 1);
        assertThat(testDetailInvoice.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testDetailInvoice.getPrice()).isEqualTo(DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateDetailInvoiceWithPatch() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        int databaseSizeBeforeUpdate = detailInvoiceRepository.findAll().size();

        // Update the detailInvoice using partial update
        DetailInvoice partialUpdatedDetailInvoice = new DetailInvoice();
        partialUpdatedDetailInvoice.setId(detailInvoice.getId());

        partialUpdatedDetailInvoice.quantity(UPDATED_QUANTITY).price(UPDATED_PRICE);

        restDetailInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDetailInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDetailInvoice))
            )
            .andExpect(status().isOk());

        // Validate the DetailInvoice in the database
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeUpdate);
        DetailInvoice testDetailInvoice = detailInvoiceList.get(detailInvoiceList.size() - 1);
        assertThat(testDetailInvoice.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testDetailInvoice.getPrice()).isEqualTo(UPDATED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingDetailInvoice() throws Exception {
        int databaseSizeBeforeUpdate = detailInvoiceRepository.findAll().size();
        detailInvoice.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDetailInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, detailInvoice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailInvoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailInvoice in the database
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDetailInvoice() throws Exception {
        int databaseSizeBeforeUpdate = detailInvoiceRepository.findAll().size();
        detailInvoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(detailInvoice))
            )
            .andExpect(status().isBadRequest());

        // Validate the DetailInvoice in the database
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDetailInvoice() throws Exception {
        int databaseSizeBeforeUpdate = detailInvoiceRepository.findAll().size();
        detailInvoice.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDetailInvoiceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(detailInvoice))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DetailInvoice in the database
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDetailInvoice() throws Exception {
        // Initialize the database
        detailInvoiceRepository.saveAndFlush(detailInvoice);

        int databaseSizeBeforeDelete = detailInvoiceRepository.findAll().size();

        // Delete the detailInvoice
        restDetailInvoiceMockMvc
            .perform(delete(ENTITY_API_URL_ID, detailInvoice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DetailInvoice> detailInvoiceList = detailInvoiceRepository.findAll();
        assertThat(detailInvoiceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
