package com.secitriy.analyzer.web.rest;

import com.secitriy.analyzer.domain.State;
import com.secitriy.analyzer.repository.StateRepository;
import com.secitriy.analyzer.service.StateService;
import com.secitriy.analyzer.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.secitriy.analyzer.domain.State}.
 */
@RestController
@RequestMapping("/api/states")
public class StateResource {

    private final Logger log = LoggerFactory.getLogger(StateResource.class);

    private static final String ENTITY_NAME = "state";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StateService stateService;

    private final StateRepository stateRepository;

    public StateResource(StateService stateService, StateRepository stateRepository) {
        this.stateService = stateService;
        this.stateRepository = stateRepository;
    }

    /**
     * {@code POST  /states} : Create a new state.
     *
     * @param state the state to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new state, or with status {@code 400 (Bad Request)} if the state has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<State> createState(@RequestBody State state) throws URISyntaxException {
        log.debug("REST request to save State : {}", state);
        if (state.getId() != null) {
            throw new BadRequestAlertException("A new state cannot already have an ID", ENTITY_NAME, "idexists");
        }
        state = stateService.save(state);
        return ResponseEntity.created(new URI("/api/states/" + state.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, state.getId().toString()))
            .body(state);
    }

    /**
     * {@code PUT  /states/:id} : Updates an existing state.
     *
     * @param id the id of the state to save.
     * @param state the state to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated state,
     * or with status {@code 400 (Bad Request)} if the state is not valid,
     * or with status {@code 500 (Internal Server Error)} if the state couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<State> updateState(@PathVariable(value = "id", required = false) final Long id, @RequestBody State state)
        throws URISyntaxException {
        log.debug("REST request to update State : {}, {}", id, state);
        if (state.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, state.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        state = stateService.update(state);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, state.getId().toString()))
            .body(state);
    }

    /**
     * {@code PATCH  /states/:id} : Partial updates given fields of an existing state, field will ignore if it is null
     *
     * @param id the id of the state to save.
     * @param state the state to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated state,
     * or with status {@code 400 (Bad Request)} if the state is not valid,
     * or with status {@code 404 (Not Found)} if the state is not found,
     * or with status {@code 500 (Internal Server Error)} if the state couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<State> partialUpdateState(@PathVariable(value = "id", required = false) final Long id, @RequestBody State state)
        throws URISyntaxException {
        log.debug("REST request to partial update State partially : {}, {}", id, state);
        if (state.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, state.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<State> result = stateService.partialUpdate(state);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, state.getId().toString())
        );
    }

    /**
     * {@code GET  /states} : get all the states.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of states in body.
     */
    @GetMapping("")
    public List<State> getAllStates(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("userattributes-is-null".equals(filter)) {
            log.debug("REST request to get all States where userAttributes is null");
            return stateService.findAllWhereUserAttributesIsNull();
        }
        log.debug("REST request to get all States");
        return stateService.findAll();
    }

    /**
     * {@code GET  /states/:id} : get the "id" state.
     *
     * @param id the id of the state to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the state, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<State> getState(@PathVariable("id") Long id) {
        log.debug("REST request to get State : {}", id);
        Optional<State> state = stateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(state);
    }

    /**
     * {@code DELETE  /states/:id} : delete the "id" state.
     *
     * @param id the id of the state to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteState(@PathVariable("id") Long id) {
        log.debug("REST request to delete State : {}", id);
        stateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
