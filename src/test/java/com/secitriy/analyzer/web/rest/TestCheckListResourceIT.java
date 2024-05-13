package com.secitriy.analyzer.web.rest;

import static com.secitriy.analyzer.domain.TestCheckListAsserts.*;
import static com.secitriy.analyzer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secitriy.analyzer.IntegrationTest;
import com.secitriy.analyzer.domain.TestCheckList;
import com.secitriy.analyzer.repository.TestCheckListRepository;
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
 * Integration tests for the {@link TestCheckListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TestCheckListResourceIT {

    private static final String ENTITY_API_URL = "/api/test-check-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TestCheckListRepository testCheckListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTestCheckListMockMvc;

    private TestCheckList testCheckList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCheckList createEntity(EntityManager em) {
        TestCheckList testCheckList = new TestCheckList();
        return testCheckList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TestCheckList createUpdatedEntity(EntityManager em) {
        TestCheckList testCheckList = new TestCheckList();
        return testCheckList;
    }

    @BeforeEach
    public void initTest() {
        testCheckList = createEntity(em);
    }

    @Test
    @Transactional
    void createTestCheckList() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TestCheckList
        var returnedTestCheckList = om.readValue(
            restTestCheckListMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testCheckList)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TestCheckList.class
        );

        // Validate the TestCheckList in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTestCheckListUpdatableFieldsEquals(returnedTestCheckList, getPersistedTestCheckList(returnedTestCheckList));
    }

    @Test
    @Transactional
    void createTestCheckListWithExistingId() throws Exception {
        // Create the TestCheckList with an existing ID
        testCheckList.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTestCheckListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testCheckList)))
            .andExpect(status().isBadRequest());

        // Validate the TestCheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllTestCheckLists() throws Exception {
        // Initialize the database
        testCheckListRepository.saveAndFlush(testCheckList);

        // Get all the testCheckListList
        restTestCheckListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(testCheckList.getId().intValue())));
    }

    @Test
    @Transactional
    void getTestCheckList() throws Exception {
        // Initialize the database
        testCheckListRepository.saveAndFlush(testCheckList);

        // Get the testCheckList
        restTestCheckListMockMvc
            .perform(get(ENTITY_API_URL_ID, testCheckList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(testCheckList.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingTestCheckList() throws Exception {
        // Get the testCheckList
        restTestCheckListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTestCheckList() throws Exception {
        // Initialize the database
        testCheckListRepository.saveAndFlush(testCheckList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testCheckList
        TestCheckList updatedTestCheckList = testCheckListRepository.findById(testCheckList.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTestCheckList are not directly saved in db
        em.detach(updatedTestCheckList);

        restTestCheckListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTestCheckList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTestCheckList))
            )
            .andExpect(status().isOk());

        // Validate the TestCheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTestCheckListToMatchAllProperties(updatedTestCheckList);
    }

    @Test
    @Transactional
    void putNonExistingTestCheckList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCheckList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCheckListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, testCheckList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testCheckList))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTestCheckList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCheckList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCheckListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(testCheckList))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTestCheckList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCheckList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCheckListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(testCheckList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTestCheckListWithPatch() throws Exception {
        // Initialize the database
        testCheckListRepository.saveAndFlush(testCheckList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testCheckList using partial update
        TestCheckList partialUpdatedTestCheckList = new TestCheckList();
        partialUpdatedTestCheckList.setId(testCheckList.getId());

        restTestCheckListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCheckList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTestCheckList))
            )
            .andExpect(status().isOk());

        // Validate the TestCheckList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTestCheckListUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTestCheckList, testCheckList),
            getPersistedTestCheckList(testCheckList)
        );
    }

    @Test
    @Transactional
    void fullUpdateTestCheckListWithPatch() throws Exception {
        // Initialize the database
        testCheckListRepository.saveAndFlush(testCheckList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the testCheckList using partial update
        TestCheckList partialUpdatedTestCheckList = new TestCheckList();
        partialUpdatedTestCheckList.setId(testCheckList.getId());

        restTestCheckListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTestCheckList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTestCheckList))
            )
            .andExpect(status().isOk());

        // Validate the TestCheckList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTestCheckListUpdatableFieldsEquals(partialUpdatedTestCheckList, getPersistedTestCheckList(partialUpdatedTestCheckList));
    }

    @Test
    @Transactional
    void patchNonExistingTestCheckList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCheckList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTestCheckListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, testCheckList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(testCheckList))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTestCheckList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCheckList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCheckListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(testCheckList))
            )
            .andExpect(status().isBadRequest());

        // Validate the TestCheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTestCheckList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        testCheckList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTestCheckListMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(testCheckList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TestCheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTestCheckList() throws Exception {
        // Initialize the database
        testCheckListRepository.saveAndFlush(testCheckList);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the testCheckList
        restTestCheckListMockMvc
            .perform(delete(ENTITY_API_URL_ID, testCheckList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return testCheckListRepository.count();
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

    protected TestCheckList getPersistedTestCheckList(TestCheckList testCheckList) {
        return testCheckListRepository.findById(testCheckList.getId()).orElseThrow();
    }

    protected void assertPersistedTestCheckListToMatchAllProperties(TestCheckList expectedTestCheckList) {
        assertTestCheckListAllPropertiesEquals(expectedTestCheckList, getPersistedTestCheckList(expectedTestCheckList));
    }

    protected void assertPersistedTestCheckListToMatchUpdatableProperties(TestCheckList expectedTestCheckList) {
        assertTestCheckListAllUpdatablePropertiesEquals(expectedTestCheckList, getPersistedTestCheckList(expectedTestCheckList));
    }
}
