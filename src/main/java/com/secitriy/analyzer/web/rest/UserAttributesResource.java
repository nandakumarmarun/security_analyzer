package com.secitriy.analyzer.web.rest;

import com.secitriy.analyzer.domain.UserAttributes;
import com.secitriy.analyzer.repository.UserAttributesRepository;
import com.secitriy.analyzer.service.UserAttributesService;
import com.secitriy.analyzer.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.secitriy.analyzer.domain.UserAttributes}.
 */
@RestController
@RequestMapping("/api/user-attributes")
public class UserAttributesResource {

    private final Logger log = LoggerFactory.getLogger(UserAttributesResource.class);

    private static final String ENTITY_NAME = "userAttributes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserAttributesService userAttributesService;

    private final UserAttributesRepository userAttributesRepository;

    public UserAttributesResource(UserAttributesService userAttributesService, UserAttributesRepository userAttributesRepository) {
        this.userAttributesService = userAttributesService;
        this.userAttributesRepository = userAttributesRepository;
    }

    /**
     * {@code POST  /user-attributes} : Create a new userAttributes.
     *
     * @param userAttributes the userAttributes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userAttributes, or with status {@code 400 (Bad Request)} if the userAttributes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserAttributes> createUserAttributes(@Valid @RequestBody UserAttributes userAttributes)
        throws URISyntaxException {
        log.debug("REST request to save UserAttributes : {}", userAttributes);
        if (userAttributes.getId() != null) {
            throw new BadRequestAlertException("A new userAttributes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userAttributes = userAttributesService.save(userAttributes);
        return ResponseEntity.created(new URI("/api/user-attributes/" + userAttributes.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userAttributes.getId().toString()))
            .body(userAttributes);
    }

    /**
     * {@code PUT  /user-attributes/:id} : Updates an existing userAttributes.
     *
     * @param id the id of the userAttributes to save.
     * @param userAttributes the userAttributes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAttributes,
     * or with status {@code 400 (Bad Request)} if the userAttributes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userAttributes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserAttributes> updateUserAttributes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserAttributes userAttributes
    ) throws URISyntaxException {
        log.debug("REST request to update UserAttributes : {}, {}", id, userAttributes);
        if (userAttributes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAttributes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAttributesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userAttributes = userAttributesService.update(userAttributes);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAttributes.getId().toString()))
            .body(userAttributes);
    }

    /**
     * {@code PATCH  /user-attributes/:id} : Partial updates given fields of an existing userAttributes, field will ignore if it is null
     *
     * @param id the id of the userAttributes to save.
     * @param userAttributes the userAttributes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userAttributes,
     * or with status {@code 400 (Bad Request)} if the userAttributes is not valid,
     * or with status {@code 404 (Not Found)} if the userAttributes is not found,
     * or with status {@code 500 (Internal Server Error)} if the userAttributes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserAttributes> partialUpdateUserAttributes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserAttributes userAttributes
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserAttributes partially : {}, {}", id, userAttributes);
        if (userAttributes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userAttributes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userAttributesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserAttributes> result = userAttributesService.partialUpdate(userAttributes);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userAttributes.getId().toString())
        );
    }

    /**
     * {@code GET  /user-attributes} : get all the userAttributes.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userAttributes in body.
     */
    @GetMapping("")
    public List<UserAttributes> getAllUserAttributes(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("applicationuser-is-null".equals(filter)) {
            log.debug("REST request to get all UserAttributess where applicationUser is null");
            return userAttributesService.findAllWhereApplicationUserIsNull();
        }
        log.debug("REST request to get all UserAttributes");
        return userAttributesService.findAll();
    }

    /**
     * {@code GET  /user-attributes/:id} : get the "id" userAttributes.
     *
     * @param id the id of the userAttributes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userAttributes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserAttributes> getUserAttributes(@PathVariable("id") Long id) {
        log.debug("REST request to get UserAttributes : {}", id);
        Optional<UserAttributes> userAttributes = userAttributesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userAttributes);
    }

    /**
     * {@code DELETE  /user-attributes/:id} : delete the "id" userAttributes.
     *
     * @param id the id of the userAttributes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAttributes(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserAttributes : {}", id);
        userAttributesService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
