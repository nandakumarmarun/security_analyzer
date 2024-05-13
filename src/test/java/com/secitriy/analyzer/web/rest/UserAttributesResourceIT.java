package com.secitriy.analyzer.web.rest;

import static com.secitriy.analyzer.domain.UserAttributesAsserts.*;
import static com.secitriy.analyzer.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secitriy.analyzer.IntegrationTest;
import com.secitriy.analyzer.domain.UserAttributes;
import com.secitriy.analyzer.repository.UserAttributesRepository;
import com.secitriy.analyzer.service.UserAttributesService;
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
 * Integration tests for the {@link UserAttributesResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserAttributesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/user-attributes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserAttributesRepository userAttributesRepository;

    @Mock
    private UserAttributesRepository userAttributesRepositoryMock;

    @Mock
    private UserAttributesService userAttributesServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserAttributesMockMvc;

    private UserAttributes userAttributes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAttributes createEntity(EntityManager em) {
        UserAttributes userAttributes = new UserAttributes()
            .name(DEFAULT_NAME)
            .phone(DEFAULT_PHONE)
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS);
        return userAttributes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserAttributes createUpdatedEntity(EntityManager em) {
        UserAttributes userAttributes = new UserAttributes()
            .name(UPDATED_NAME)
            .phone(UPDATED_PHONE)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS);
        return userAttributes;
    }

    @BeforeEach
    public void initTest() {
        userAttributes = createEntity(em);
    }

    @Test
    @Transactional
    void createUserAttributes() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserAttributes
        var returnedUserAttributes = om.readValue(
            restUserAttributesMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAttributes)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserAttributes.class
        );

        // Validate the UserAttributes in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserAttributesUpdatableFieldsEquals(returnedUserAttributes, getPersistedUserAttributes(returnedUserAttributes));
    }

    @Test
    @Transactional
    void createUserAttributesWithExistingId() throws Exception {
        // Create the UserAttributes with an existing ID
        userAttributes.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserAttributesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAttributes)))
            .andExpect(status().isBadRequest());

        // Validate the UserAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userAttributes.setName(null);

        // Create the UserAttributes, which fails.

        restUserAttributesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAttributes)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserAttributes() throws Exception {
        // Initialize the database
        userAttributesRepository.saveAndFlush(userAttributes);

        // Get all the userAttributesList
        restUserAttributesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userAttributes.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserAttributesWithEagerRelationshipsIsEnabled() throws Exception {
        when(userAttributesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserAttributesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userAttributesServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserAttributesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userAttributesServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserAttributesMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userAttributesRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserAttributes() throws Exception {
        // Initialize the database
        userAttributesRepository.saveAndFlush(userAttributes);

        // Get the userAttributes
        restUserAttributesMockMvc
            .perform(get(ENTITY_API_URL_ID, userAttributes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userAttributes.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS));
    }

    @Test
    @Transactional
    void getNonExistingUserAttributes() throws Exception {
        // Get the userAttributes
        restUserAttributesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserAttributes() throws Exception {
        // Initialize the database
        userAttributesRepository.saveAndFlush(userAttributes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAttributes
        UserAttributes updatedUserAttributes = userAttributesRepository.findById(userAttributes.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserAttributes are not directly saved in db
        em.detach(updatedUserAttributes);
        updatedUserAttributes.name(UPDATED_NAME).phone(UPDATED_PHONE).email(UPDATED_EMAIL).address(UPDATED_ADDRESS);

        restUserAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserAttributes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserAttributes))
            )
            .andExpect(status().isOk());

        // Validate the UserAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserAttributesToMatchAllProperties(updatedUserAttributes);
    }

    @Test
    @Transactional
    void putNonExistingUserAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAttributes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userAttributes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAttributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAttributes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAttributesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userAttributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAttributes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAttributesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userAttributes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserAttributesWithPatch() throws Exception {
        // Initialize the database
        userAttributesRepository.saveAndFlush(userAttributes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAttributes using partial update
        UserAttributes partialUpdatedUserAttributes = new UserAttributes();
        partialUpdatedUserAttributes.setId(userAttributes.getId());

        partialUpdatedUserAttributes.name(UPDATED_NAME);

        restUserAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAttributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAttributes))
            )
            .andExpect(status().isOk());

        // Validate the UserAttributes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAttributesUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserAttributes, userAttributes),
            getPersistedUserAttributes(userAttributes)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserAttributesWithPatch() throws Exception {
        // Initialize the database
        userAttributesRepository.saveAndFlush(userAttributes);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userAttributes using partial update
        UserAttributes partialUpdatedUserAttributes = new UserAttributes();
        partialUpdatedUserAttributes.setId(userAttributes.getId());

        partialUpdatedUserAttributes.name(UPDATED_NAME).phone(UPDATED_PHONE).email(UPDATED_EMAIL).address(UPDATED_ADDRESS);

        restUserAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserAttributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserAttributes))
            )
            .andExpect(status().isOk());

        // Validate the UserAttributes in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserAttributesUpdatableFieldsEquals(partialUpdatedUserAttributes, getPersistedUserAttributes(partialUpdatedUserAttributes));
    }

    @Test
    @Transactional
    void patchNonExistingUserAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAttributes.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userAttributes.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAttributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAttributes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAttributesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userAttributes))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserAttributes() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userAttributes.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserAttributesMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userAttributes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserAttributes in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserAttributes() throws Exception {
        // Initialize the database
        userAttributesRepository.saveAndFlush(userAttributes);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userAttributes
        restUserAttributesMockMvc
            .perform(delete(ENTITY_API_URL_ID, userAttributes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userAttributesRepository.count();
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

    protected UserAttributes getPersistedUserAttributes(UserAttributes userAttributes) {
        return userAttributesRepository.findById(userAttributes.getId()).orElseThrow();
    }

    protected void assertPersistedUserAttributesToMatchAllProperties(UserAttributes expectedUserAttributes) {
        assertUserAttributesAllPropertiesEquals(expectedUserAttributes, getPersistedUserAttributes(expectedUserAttributes));
    }

    protected void assertPersistedUserAttributesToMatchUpdatableProperties(UserAttributes expectedUserAttributes) {
        assertUserAttributesAllUpdatablePropertiesEquals(expectedUserAttributes, getPersistedUserAttributes(expectedUserAttributes));
    }
}
