package com.secitriy.analyzer.web.rest;

import static com.secitriy.analyzer.domain.TestCheckLisItemAsserts.*;
import static com.secitriy.analyzer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secitriy.analyzer.IntegrationTest;
import com.secitriy.analyzer.domain.TestCheckLisItem;
import com.secitriy.analyzer.repository.TestCheckLisItemRepository;
import com.secitriy.analyzer.service.TestCheckLisItemService;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TestCheckLisItemResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TestCheckLisItemResourceIT {

    private static final Boolean DEFAULT_MARKED = false;
    private static final Boolean UPDATED_MARKED = true;

    private static final String ENTITY_API_URL = "/api/test-check-lis-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TestCheckLisItemRepository testCheckLisItemRepository;

    @Mock
    private TestCheckLisItemRepository testCheckLisItemRepositoryMock;

    @Mock
    private TestCheckLisItemService testCheckLisItemServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCheckLisItemMockMvc;

    private TestCheckLisItem testCheckLisItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCheckLisItem createEntity(EntityManager em) {
        TestCheckLisItem testCheckLisItem = new TestCheckLisItem().marked(DEFAULT_MARKED);
        return testCheckLisItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCheckLisItem createUpdatedEntity(EntityManager em) {
        TestCheckLisItem testCheckLisItem = new TestCheckLisItem().marked(UPDATED_MARKED);
        return testCheckLisItem;
    }

    @BeforeEach
    public void initTest() {
        testCheckLisItem = createEntity(em);
    }

    @Test
    @Transactional
    void createTestCheckLisItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TestCheckLisItem
        var returnedTestCheckLisItem = om.readValue(
            restTestCheckLisItemMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testCheckLisItem)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TestCheckLisItem.class
        );

        // Validate the TestCheckLisItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTestCheckLisItemUpdatableFieldsEquals(returnedTestCheckLisItem, getPersistedTestCheckLisItem(returnedTestCheckLisItem));
    }

    @Test
    @Transactional
    void createTestCheckLisItemWithExistingId() throws Exception {
        // Create the TestCheckLisItem with an existing ID
        testCheckLisItem.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCheckLisItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testCheckLisItem)))
            .andExpect(status().isBadRequest());

        // Validate the TestCheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestCheckLisItems() throws Exception {
        // Initialize the database
        testCheckLisItemRepository.saveAndFlush(testCheckLisItem);

        // Get all the testCheckLisItemList
        restTestCheckLisItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCheckLisItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].marked").value(hasItem(DEFAULT_MARKED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTestCheckLisItemsWithEagerRelationshipsIsEnabled() throws Exception {
        when(testCheckLisItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTestCheckLisItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(testCheckLisItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTestCheckLisItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(testCheckLisItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTestCheckLisItemMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(testCheckLisItemRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTestCheckLisItem() throws Exception {
        // Initialize the database
        testCheckLisItemRepository.saveAndFlush(testCheckLisItem);

        // Get the testCheckLisItem
        restTestCheckLisItemMockMvc
            .perform(get(ENTITY_API_URL_ID, testCheckLisItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCheckLisItem.getId().intValue()))
            .andExpect(jsonPath("$.marked").value(DEFAULT_MARKED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingTestCheckLisItem() throws Exception {
        // Get the testCheckLisItem
        restTestCheckLisItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCheckLisItem() throws Exception {
        // Initialize the database
        testCheckLisItemRepository.saveAndFlush(testCheckLisItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testCheckLisItem
        TestCheckLisItem updatedTestCheckLisItem = testCheckLisItemRepository.findById(testCheckLisItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTestCheckLisItem are not directly saved in db
        em.detach(updatedTestCheckLisItem);
        updatedTestCheckLisItem.marked(UPDATED_MARKED);

        restTestCheckLisItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestCheckLisItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTestCheckLisItem))
            )
            .andExpect(status().isOk());

        // Validate the TestCheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTestCheckLisItemToMatchAllProperties(updatedTestCheckLisItem);
    }

    @Test
    @Transactional
    void putNonExistingTestCheckLisItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCheckLisItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCheckLisItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCheckLisItem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testCheckLisItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCheckLisItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCheckLisItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCheckLisItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testCheckLisItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCheckLisItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCheckLisItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCheckLisItemMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testCheckLisItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCheckLisItemWithPatch() throws Exception {
        // Initialize the database
        testCheckLisItemRepository.saveAndFlush(testCheckLisItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testCheckLisItem using partial update
        TestCheckLisItem partialUpdatedTestCheckLisItem = new TestCheckLisItem();
        partialUpdatedTestCheckLisItem.setId(testCheckLisItem.getId());

        restTestCheckLisItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCheckLisItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTestCheckLisItem))
            )
            .andExpect(status().isOk());

        // Validate the TestCheckLisItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTestCheckLisItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTestCheckLisItem, testCheckLisItem),
            getPersistedTestCheckLisItem(testCheckLisItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateTestCheckLisItemWithPatch() throws Exception {
        // Initialize the database
        testCheckLisItemRepository.saveAndFlush(testCheckLisItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testCheckLisItem using partial update
        TestCheckLisItem partialUpdatedTestCheckLisItem = new TestCheckLisItem();
        partialUpdatedTestCheckLisItem.setId(testCheckLisItem.getId());

        partialUpdatedTestCheckLisItem.marked(UPDATED_MARKED);

        restTestCheckLisItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCheckLisItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTestCheckLisItem))
            )
            .andExpect(status().isOk());

        // Validate the TestCheckLisItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTestCheckLisItemUpdatableFieldsEquals(
            partialUpdatedTestCheckLisItem,
            getPersistedTestCheckLisItem(partialUpdatedTestCheckLisItem)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTestCheckLisItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCheckLisItem.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCheckLisItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCheckLisItem.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(testCheckLisItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCheckLisItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCheckLisItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCheckLisItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(testCheckLisItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCheckLisItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCheckLisItem.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCheckLisItemMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(testCheckLisItem)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCheckLisItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCheckLisItem() throws Exception {
        // Initialize the database
        testCheckLisItemRepository.saveAndFlush(testCheckLisItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the testCheckLisItem
        restTestCheckLisItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCheckLisItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return testCheckLisItemRepository.count();
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

    protected TestCheckLisItem getPersistedTestCheckLisItem(TestCheckLisItem testCheckLisItem) {
        return testCheckLisItemRepository.findById(testCheckLisItem.getId()).orElseThrow();
    }

    protected void assertPersistedTestCheckLisItemToMatchAllProperties(TestCheckLisItem expectedTestCheckLisItem) {
        assertTestCheckLisItemAllPropertiesEquals(expectedTestCheckLisItem, getPersistedTestCheckLisItem(expectedTestCheckLisItem));
    }

    protected void assertPersistedTestCheckLisItemToMatchUpdatableProperties(TestCheckLisItem expectedTestCheckLisItem) {
        assertTestCheckLisItemAllUpdatablePropertiesEquals(
            expectedTestCheckLisItem,
            getPersistedTestCheckLisItem(expectedTestCheckLisItem)
        );
    }
}
