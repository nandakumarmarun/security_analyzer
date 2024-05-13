package com.secitriy.analyzer.web.rest;

import static com.secitriy.analyzer.domain.SecurityTestAsserts.*;
import static com.secitriy.analyzer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secitriy.analyzer.IntegrationTest;
import com.secitriy.analyzer.domain.SecurityTest;
import com.secitriy.analyzer.domain.enumeration.SecurityLevel;
import com.secitriy.analyzer.repository.SecurityTestRepository;
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
 * Integration tests for the {@link SecurityTestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SecurityTestResourceIT {

    private static final String DEFAULT_TEST_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_TEST_STATUS = "BBBBBBBBBB";

    private static final Double DEFAULT_TEST_SCORE = 1D;
    private static final Double UPDATED_TEST_SCORE = 2D;

    private static final SecurityLevel DEFAULT_SECURITY_LEVEL = SecurityLevel.HIGH;
    private static final SecurityLevel UPDATED_SECURITY_LEVEL = SecurityLevel.MODERATE;

    private static final String ENTITY_API_URL = "/api/security-tests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SecurityTestRepository securityTestRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSecurityTestMockMvc;

    private SecurityTest securityTest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityTest createEntity(EntityManager em) {
        SecurityTest securityTest = new SecurityTest()
            .testStatus(DEFAULT_TEST_STATUS)
            .testScore(DEFAULT_TEST_SCORE)
            .securityLevel(DEFAULT_SECURITY_LEVEL);
        return securityTest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SecurityTest createUpdatedEntity(EntityManager em) {
        SecurityTest securityTest = new SecurityTest()
            .testStatus(UPDATED_TEST_STATUS)
            .testScore(UPDATED_TEST_SCORE)
            .securityLevel(UPDATED_SECURITY_LEVEL);
        return securityTest;
    }

    @BeforeEach
    public void initTest() {
        securityTest = createEntity(em);
    }

    @Test
    @Transactional
    void createSecurityTest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SecurityTest
        var returnedSecurityTest = om.readValue(
            restSecurityTestMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(securityTest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SecurityTest.class
        );

        // Validate the SecurityTest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSecurityTestUpdatableFieldsEquals(returnedSecurityTest, getPersistedSecurityTest(returnedSecurityTest));
    }

    @Test
    @Transactional
    void createSecurityTestWithExistingId() throws Exception {
        // Create the SecurityTest with an existing ID
        securityTest.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSecurityTestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(securityTest)))
            .andExpect(status().isBadRequest());

        // Validate the SecurityTest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSecurityTests() throws Exception {
        // Initialize the database
        securityTestRepository.saveAndFlush(securityTest);

        // Get all the securityTestList
        restSecurityTestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(securityTest.getId().intValue())))
            .andExpect(jsonPath("$.[*].testStatus").value(hasItem(DEFAULT_TEST_STATUS)))
            .andExpect(jsonPath("$.[*].testScore").value(hasItem(DEFAULT_TEST_SCORE.doubleValue())))
            .andExpect(jsonPath("$.[*].securityLevel").value(hasItem(DEFAULT_SECURITY_LEVEL.toString())));
    }

    @Test
    @Transactional
    void getSecurityTest() throws Exception {
        // Initialize the database
        securityTestRepository.saveAndFlush(securityTest);

        // Get the securityTest
        restSecurityTestMockMvc
            .perform(get(ENTITY_API_URL_ID, securityTest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(securityTest.getId().intValue()))
            .andExpect(jsonPath("$.testStatus").value(DEFAULT_TEST_STATUS))
            .andExpect(jsonPath("$.testScore").value(DEFAULT_TEST_SCORE.doubleValue()))
            .andExpect(jsonPath("$.securityLevel").value(DEFAULT_SECURITY_LEVEL.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSecurityTest() throws Exception {
        // Get the securityTest
        restSecurityTestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSecurityTest() throws Exception {
        // Initialize the database
        securityTestRepository.saveAndFlush(securityTest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the securityTest
        SecurityTest updatedSecurityTest = securityTestRepository.findById(securityTest.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSecurityTest are not directly saved in db
        em.detach(updatedSecurityTest);
        updatedSecurityTest.testStatus(UPDATED_TEST_STATUS).testScore(UPDATED_TEST_SCORE).securityLevel(UPDATED_SECURITY_LEVEL);

        restSecurityTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSecurityTest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedSecurityTest))
            )
            .andExpect(status().isOk());

        // Validate the SecurityTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSecurityTestToMatchAllProperties(updatedSecurityTest);
    }

    @Test
    @Transactional
    void putNonExistingSecurityTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        securityTest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, securityTest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(securityTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSecurityTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        securityTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityTestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(securityTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSecurityTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        securityTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityTestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(securityTest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSecurityTestWithPatch() throws Exception {
        // Initialize the database
        securityTestRepository.saveAndFlush(securityTest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the securityTest using partial update
        SecurityTest partialUpdatedSecurityTest = new SecurityTest();
        partialUpdatedSecurityTest.setId(securityTest.getId());

        partialUpdatedSecurityTest.testStatus(UPDATED_TEST_STATUS).securityLevel(UPDATED_SECURITY_LEVEL);

        restSecurityTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSecurityTest))
            )
            .andExpect(status().isOk());

        // Validate the SecurityTest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSecurityTestUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSecurityTest, securityTest),
            getPersistedSecurityTest(securityTest)
        );
    }

    @Test
    @Transactional
    void fullUpdateSecurityTestWithPatch() throws Exception {
        // Initialize the database
        securityTestRepository.saveAndFlush(securityTest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the securityTest using partial update
        SecurityTest partialUpdatedSecurityTest = new SecurityTest();
        partialUpdatedSecurityTest.setId(securityTest.getId());

        partialUpdatedSecurityTest.testStatus(UPDATED_TEST_STATUS).testScore(UPDATED_TEST_SCORE).securityLevel(UPDATED_SECURITY_LEVEL);

        restSecurityTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSecurityTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSecurityTest))
            )
            .andExpect(status().isOk());

        // Validate the SecurityTest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSecurityTestUpdatableFieldsEquals(partialUpdatedSecurityTest, getPersistedSecurityTest(partialUpdatedSecurityTest));
    }

    @Test
    @Transactional
    void patchNonExistingSecurityTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        securityTest.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSecurityTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, securityTest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(securityTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSecurityTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        securityTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityTestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(securityTest))
            )
            .andExpect(status().isBadRequest());

        // Validate the SecurityTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSecurityTest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        securityTest.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSecurityTestMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(securityTest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SecurityTest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSecurityTest() throws Exception {
        // Initialize the database
        securityTestRepository.saveAndFlush(securityTest);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the securityTest
        restSecurityTestMockMvc
            .perform(delete(ENTITY_API_URL_ID, securityTest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return securityTestRepository.count();
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

    protected SecurityTest getPersistedSecurityTest(SecurityTest securityTest) {
        return securityTestRepository.findById(securityTest.getId()).orElseThrow();
    }

    protected void assertPersistedSecurityTestToMatchAllProperties(SecurityTest expectedSecurityTest) {
        assertSecurityTestAllPropertiesEquals(expectedSecurityTest, getPersistedSecurityTest(expectedSecurityTest));
    }

    protected void assertPersistedSecurityTestToMatchUpdatableProperties(SecurityTest expectedSecurityTest) {
        assertSecurityTestAllUpdatablePropertiesEquals(expectedSecurityTest, getPersistedSecurityTest(expectedSecurityTest));
    }
}
