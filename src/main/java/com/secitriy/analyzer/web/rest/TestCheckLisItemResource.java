package com.secitriy.analyzer.web.rest;

import com.secitriy.analyzer.domain.TestCheckLisItem;
import com.secitriy.analyzer.repository.TestCheckLisItemRepository;
import com.secitriy.analyzer.service.TestCheckLisItemService;
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
 * REST controller for managing {@link com.secitriy.analyzer.domain.TestCheckLisItem}.
 */
@RestController
@RequestMapping("/api/test-check-lis-items")
public class TestCheckLisItemResource {

    private final Logger log = LoggerFactory.getLogger(TestCheckLisItemResource.class);

    private static final String ENTITY_NAME = "testCheckLisItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCheckLisItemService testCheckLisItemService;

    private final TestCheckLisItemRepository testCheckLisItemRepository;

    public TestCheckLisItemResource(
        TestCheckLisItemService testCheckLisItemService,
        TestCheckLisItemRepository testCheckLisItemRepository
    ) {
        this.testCheckLisItemService = testCheckLisItemService;
        this.testCheckLisItemRepository = testCheckLisItemRepository;
    }

    /**
     * {@code POST  /test-check-lis-items} : Create a new testCheckLisItem.
     *
     * @param testCheckLisItem the testCheckLisItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCheckLisItem, or with status {@code 400 (Bad Request)} if the testCheckLisItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TestCheckLisItem> createTestCheckLisItem(@RequestBody TestCheckLisItem testCheckLisItem)
        throws URISyntaxException {
        log.debug("REST request to save TestCheckLisItem : {}", testCheckLisItem);
        if (testCheckLisItem.getId() != null) {
            throw new BadRequestAlertException("A new testCheckLisItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        testCheckLisItem = testCheckLisItemService.save(testCheckLisItem);
        return ResponseEntity.created(new URI("/api/test-check-lis-items/" + testCheckLisItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, testCheckLisItem.getId().toString()))
            .body(testCheckLisItem);
    }

    /**
     * {@code PUT  /test-check-lis-items/:id} : Updates an existing testCheckLisItem.
     *
     * @param id the id of the testCheckLisItem to save.
     * @param testCheckLisItem the testCheckLisItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCheckLisItem,
     * or with status {@code 400 (Bad Request)} if the testCheckLisItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCheckLisItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestCheckLisItem> updateTestCheckLisItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestCheckLisItem testCheckLisItem
    ) throws URISyntaxException {
        log.debug("REST request to update TestCheckLisItem : {}, {}", id, testCheckLisItem);
        if (testCheckLisItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCheckLisItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCheckLisItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        testCheckLisItem = testCheckLisItemService.update(testCheckLisItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testCheckLisItem.getId().toString()))
            .body(testCheckLisItem);
    }

    /**
     * {@code PATCH  /test-check-lis-items/:id} : Partial updates given fields of an existing testCheckLisItem, field will ignore if it is null
     *
     * @param id the id of the testCheckLisItem to save.
     * @param testCheckLisItem the testCheckLisItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCheckLisItem,
     * or with status {@code 400 (Bad Request)} if the testCheckLisItem is not valid,
     * or with status {@code 404 (Not Found)} if the testCheckLisItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCheckLisItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCheckLisItem> partialUpdateTestCheckLisItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestCheckLisItem testCheckLisItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestCheckLisItem partially : {}, {}", id, testCheckLisItem);
        if (testCheckLisItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCheckLisItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCheckLisItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCheckLisItem> result = testCheckLisItemService.partialUpdate(testCheckLisItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testCheckLisItem.getId().toString())
        );
    }

    /**
     * {@code GET  /test-check-lis-items} : get all the testCheckLisItems.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCheckLisItems in body.
     */
    @GetMapping("")
    public List<TestCheckLisItem> getAllTestCheckLisItems(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        log.debug("REST request to get all TestCheckLisItems");
        return testCheckLisItemService.findAll();
    }

    /**
     * {@code GET  /test-check-lis-items/:id} : get the "id" testCheckLisItem.
     *
     * @param id the id of the testCheckLisItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCheckLisItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestCheckLisItem> getTestCheckLisItem(@PathVariable("id") Long id) {
        log.debug("REST request to get TestCheckLisItem : {}", id);
        Optional<TestCheckLisItem> testCheckLisItem = testCheckLisItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCheckLisItem);
    }

    /**
     * {@code DELETE  /test-check-lis-items/:id} : delete the "id" testCheckLisItem.
     *
     * @param id the id of the testCheckLisItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCheckLisItem(@PathVariable("id") Long id) {
        log.debug("REST request to delete TestCheckLisItem : {}", id);
        testCheckLisItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
