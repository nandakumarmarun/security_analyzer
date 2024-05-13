package com.secitriy.analyzer.web.rest;

import static com.secitriy.analyzer.domain.CheckListAsserts.*;
import static com.secitriy.analyzer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secitriy.analyzer.IntegrationTest;
import com.secitriy.analyzer.domain.CheckList;
import com.secitriy.analyzer.repository.CheckListRepository;
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
 * Integration tests for the {@link CheckListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CheckListResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/check-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CheckListRepository checkListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCheckListMockMvc;

    private CheckList checkList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CheckList createEntity(EntityManager em) {
        CheckList checkList = new CheckList().name(DEFAULT_NAME);
        return checkList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CheckList createUpdatedEntity(EntityManager em) {
        CheckList checkList = new CheckList().name(UPDATED_NAME);
        return checkList;
    }

    @BeforeEach
    public void initTest() {
        checkList = createEntity(em);
    }

    @Test
    @Transactional
    void createCheckList() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CheckList
        var returnedCheckList = om.readValue(
            restCheckListMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(checkList)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CheckList.class
        );

        // Validate the CheckList in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertCheckListUpdatableFieldsEquals(returnedCheckList, getPersistedCheckList(returnedCheckList));
    }

    @Test
    @Transactional
    void createCheckListWithExistingId() throws Exception {
        // Create the CheckList with an existing ID
        checkList.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCheckListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(checkList)))
            .andExpect(status().isBadRequest());

        // Validate the CheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCheckLists() throws Exception {
        // Initialize the database
        checkListRepository.saveAndFlush(checkList);

        // Get all the checkListList
        restCheckListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checkList.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCheckList() throws Exception {
        // Initialize the database
        checkListRepository.saveAndFlush(checkList);

        // Get the checkList
        restCheckListMockMvc
            .perform(get(ENTITY_API_URL_ID, checkList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(checkList.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCheckList() throws Exception {
        // Get the checkList
        restCheckListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCheckList() throws Exception {
        // Initialize the database
        checkListRepository.saveAndFlush(checkList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the checkList
        CheckList updatedCheckList = checkListRepository.findById(checkList.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCheckList are not directly saved in db
        em.detach(updatedCheckList);
        updatedCheckList.name(UPDATED_NAME);

        restCheckListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCheckList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedCheckList))
            )
            .andExpect(status().isOk());

        // Validate the CheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCheckListToMatchAllProperties(updatedCheckList);
    }

    @Test
    @Transactional
    void putNonExistingCheckList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCheckListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, checkList.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(checkList))
            )
            .andExpect(status().isBadRequest());

        // Validate the CheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCheckList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(checkList))
            )
            .andExpect(status().isBadRequest());

        // Validate the CheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCheckList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(checkList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCheckListWithPatch() throws Exception {
        // Initialize the database
        checkListRepository.saveAndFlush(checkList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the checkList using partial update
        CheckList partialUpdatedCheckList = new CheckList();
        partialUpdatedCheckList.setId(checkList.getId());

        partialUpdatedCheckList.name(UPDATED_NAME);

        restCheckListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCheckList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCheckList))
            )
            .andExpect(status().isOk());

        // Validate the CheckList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCheckListUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCheckList, checkList),
            getPersistedCheckList(checkList)
        );
    }

    @Test
    @Transactional
    void fullUpdateCheckListWithPatch() throws Exception {
        // Initialize the database
        checkListRepository.saveAndFlush(checkList);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the checkList using partial update
        CheckList partialUpdatedCheckList = new CheckList();
        partialUpdatedCheckList.setId(checkList.getId());

        partialUpdatedCheckList.name(UPDATED_NAME);

        restCheckListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCheckList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCheckList))
            )
            .andExpect(status().isOk());

        // Validate the CheckList in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCheckListUpdatableFieldsEquals(partialUpdatedCheckList, getPersistedCheckList(partialUpdatedCheckList));
    }

    @Test
    @Transactional
    void patchNonExistingCheckList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkList.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCheckListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, checkList.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(checkList))
            )
            .andExpect(status().isBadRequest());

        // Validate the CheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCheckList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(checkList))
            )
            .andExpect(status().isBadRequest());

        // Validate the CheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCheckList() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkList.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckListMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(checkList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CheckList in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCheckList() throws Exception {
        // Initialize the database
        checkListRepository.saveAndFlush(checkList);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the checkList
        restCheckListMockMvc
            .perform(delete(ENTITY_API_URL_ID, checkList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return checkListRepository.count();
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

    protected CheckList getPersistedCheckList(CheckList checkList) {
        return checkListRepository.findById(checkList.getId()).orElseThrow();
    }

    protected void assertPersistedCheckListToMatchAllProperties(CheckList expectedCheckList) {
        assertCheckListAllPropertiesEquals(expectedCheckList, getPersistedCheckList(expectedCheckList));
    }

    protected void assertPersistedCheckListToMatchUpdatableProperties(CheckList expectedCheckList) {
        assertCheckListAllUpdatablePropertiesEquals(expectedCheckList, getPersistedCheckList(expectedCheckList));
    }
}
