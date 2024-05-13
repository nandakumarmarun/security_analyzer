package com.secitriy.analyzer.web.rest;

import static com.secitriy.analyzer.domain.CheckLisItemAsserts.*;
import static com.secitriy.analyzer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secitriy.analyzer.IntegrationTest;
import com.secitriy.analyzer.domain.CheckLisItem;
import com.secitriy.analyzer.repository.CheckLisItemRepository;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CheckLisItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CheckLisItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final String ENTITY_API_URL = "/api/check-lis-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CheckLisItemRepository checkLisItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCheckLisItemMockMvc;

    private CheckLisItem checkLisItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CheckLisItem createEntity(EntityManager em) {
        CheckLisItem checkLisItem = new CheckLisItem().name(DEFAULT_NAME).value(DEFAULT_VALUE);
        return checkLisItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CheckLisItem createUpdatedEntity(EntityManager em) {
        CheckLisItem checkLisItem = new CheckLisItem().name(UPDATED_NAME).value(UPDATED_VALUE);
        return checkLisItem;
    }

    @BeforeEach
    public void initTest() {
        checkLisItem = createEntity(em);
    }

    @Test
    @Transactional
    void createCheckLisItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CheckLisItem
        var returnedCheckLisItem = om.readValue(
            restCheckLisItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(checkLisItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CheckLisItem.class
        );

        // Validate the CheckLisItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCheckLisItemUpdatableFieldsEquals(returnedCheckLisItem, getPersistedCheckLisItem(returnedCheckLisItem));
    }

    @Test
    @Transactional
    void createCheckLisItemWithExistingId() throws Exception {
        // Create the CheckLisItem with an existing ID
        checkLisItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCheckLisItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(checkLisItem)))
            .andExpect(status().isBadRequest());

        // Validate the CheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCheckLisItems() throws Exception {
        // Initialize the database
        checkLisItemRepository.saveAndFlush(checkLisItem);

        // Get all the checkLisItemList
        restCheckLisItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checkLisItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())));
    }

    @Test
    @Transactional
    void getCheckLisItem() throws Exception {
        // Initialize the database
        checkLisItemRepository.saveAndFlush(checkLisItem);

        // Get the checkLisItem
        restCheckLisItemMockMvc
            .perform(get(ENTITY_API_URL_ID, checkLisItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(checkLisItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCheckLisItem() throws Exception {
        // Get the checkLisItem
        restCheckLisItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCheckLisItem() throws Exception {
        // Initialize the database
        checkLisItemRepository.saveAndFlush(checkLisItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the checkLisItem
        CheckLisItem updatedCheckLisItem = checkLisItemRepository.findById(checkLisItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCheckLisItem are not directly saved in db
        em.detach(updatedCheckLisItem);
        updatedCheckLisItem.name(UPDATED_NAME).value(UPDATED_VALUE);

        restCheckLisItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCheckLisItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCheckLisItem))
            )
            .andExpect(status().isOk());

        // Validate the CheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCheckLisItemToMatchAllProperties(updatedCheckLisItem);
    }

    @Test
    @Transactional
    void putNonExistingCheckLisItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkLisItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCheckLisItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, checkLisItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(checkLisItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the CheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCheckLisItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkLisItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckLisItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(checkLisItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the CheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCheckLisItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkLisItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckLisItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(checkLisItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCheckLisItemWithPatch() throws Exception {
        // Initialize the database
        checkLisItemRepository.saveAndFlush(checkLisItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the checkLisItem using partial update
        CheckLisItem partialUpdatedCheckLisItem = new CheckLisItem();
        partialUpdatedCheckLisItem.setId(checkLisItem.getId());

        partialUpdatedCheckLisItem.name(UPDATED_NAME);

        restCheckLisItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCheckLisItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCheckLisItem))
            )
            .andExpect(status().isOk());

        // Validate the CheckLisItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCheckLisItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCheckLisItem, checkLisItem),
            getPersistedCheckLisItem(checkLisItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateCheckLisItemWithPatch() throws Exception {
        // Initialize the database
        checkLisItemRepository.saveAndFlush(checkLisItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the checkLisItem using partial update
        CheckLisItem partialUpdatedCheckLisItem = new CheckLisItem();
        partialUpdatedCheckLisItem.setId(checkLisItem.getId());

        partialUpdatedCheckLisItem.name(UPDATED_NAME).value(UPDATED_VALUE);

        restCheckLisItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCheckLisItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCheckLisItem))
            )
            .andExpect(status().isOk());

        // Validate the CheckLisItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCheckLisItemUpdatableFieldsEquals(partialUpdatedCheckLisItem, getPersistedCheckLisItem(partialUpdatedCheckLisItem));
    }

    @Test
    @Transactional
    void patchNonExistingCheckLisItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkLisItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCheckLisItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, checkLisItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(checkLisItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the CheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCheckLisItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkLisItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckLisItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(checkLisItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the CheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCheckLisItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkLisItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckLisItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(checkLisItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCheckLisItem() throws Exception {
        // Initialize the database
        checkLisItemRepository.saveAndFlush(checkLisItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the checkLisItem
        restCheckLisItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, checkLisItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return checkLisItemRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected CheckLisItem getPersistedCheckLisItem(CheckLisItem checkLisItem) {
        return checkLisItemRepository.findById(checkLisItem.getId()).orElseThrow();
    }

    protected void assertPersistedCheckLisItemToMatchAllProperties(CheckLisItem expectedCheckLisItem) {
        assertCheckLisItemAllPropertiesEquals(expectedCheckLisItem, getPersistedCheckLisItem(expectedCheckLisItem));
    }

    protected void assertPersistedCheckLisItemToMatchUpdatableProperties(CheckLisItem expectedCheckLisItem) {
        assertCheckLisItemAllUpdatablePropertiesEquals(expectedCheckLisItem, getPersistedCheckLisItem(expectedCheckLisItem));
    }
}
