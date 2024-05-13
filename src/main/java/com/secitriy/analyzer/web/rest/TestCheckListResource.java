package com.secitriy.analyzer.web.rest;

import com.secitriy.analyzer.domain.TestCheckList;
import com.secitriy.analyzer.repository.TestCheckListRepository;
import com.secitriy.analyzer.service.TestCheckListService;
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
 * REST controller for managing {@link com.secitriy.analyzer.domain.TestCheckList}.
 */
@RestController
@RequestMapping("/api/test-check-lists")
public class TestCheckListResource {

    private final Logger log = LoggerFactory.getLogger(TestCheckListResource.class);

    private static final String ENTITY_NAME = "testCheckList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TestCheckListService testCheckListService;

    private final TestCheckListRepository testCheckListRepository;

    public TestCheckListResource(TestCheckListService testCheckListService, TestCheckListRepository testCheckListRepository) {
        this.testCheckListService = testCheckListService;
        this.testCheckListRepository = testCheckListRepository;
    }

    /**
     * {@code POST  /test-check-lists} : Create a new testCheckList.
     *
     * @param testCheckList the testCheckList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new testCheckList, or with status {@code 400 (Bad Request)} if the testCheckList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TestCheckList> createTestCheckList(@RequestBody TestCheckList testCheckList) throws URISyntaxException {
        log.debug("REST request to save TestCheckList : {}", testCheckList);
        if (testCheckList.getId() != null) {
            throw new BadRequestAlertException("A new testCheckList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        testCheckList = testCheckListService.save(testCheckList);
        return ResponseEntity.created(new URI("/api/test-check-lists/" + testCheckList.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, testCheckList.getId().toString()))
            .body(testCheckList);
    }

    /**
     * {@code PUT  /test-check-lists/:id} : Updates an existing testCheckList.
     *
     * @param id the id of the testCheckList to save.
     * @param testCheckList the testCheckList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCheckList,
     * or with status {@code 400 (Bad Request)} if the testCheckList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the testCheckList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TestCheckList> updateTestCheckList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestCheckList testCheckList
    ) throws URISyntaxException {
        log.debug("REST request to update TestCheckList : {}, {}", id, testCheckList);
        if (testCheckList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCheckList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCheckListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        testCheckList = testCheckListService.update(testCheckList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testCheckList.getId().toString()))
            .body(testCheckList);
    }

    /**
     * {@code PATCH  /test-check-lists/:id} : Partial updates given fields of an existing testCheckList, field will ignore if it is null
     *
     * @param id the id of the testCheckList to save.
     * @param testCheckList the testCheckList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated testCheckList,
     * or with status {@code 400 (Bad Request)} if the testCheckList is not valid,
     * or with status {@code 404 (Not Found)} if the testCheckList is not found,
     * or with status {@code 500 (Internal Server Error)} if the testCheckList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TestCheckList> partialUpdateTestCheckList(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TestCheckList testCheckList
    ) throws URISyntaxException {
        log.debug("REST request to partial update TestCheckList partially : {}, {}", id, testCheckList);
        if (testCheckList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, testCheckList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!testCheckListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TestCheckList> result = testCheckListService.partialUpdate(testCheckList);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, testCheckList.getId().toString())
        );
    }

    /**
     * {@code GET  /test-check-lists} : get all the testCheckLists.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of testCheckLists in body.
     */
    @GetMapping("")
    public List<TestCheckList> getAllTestCheckLists() {
        log.debug("REST request to get all TestCheckLists");
        return testCheckListService.findAll();
    }

    /**
     * {@code GET  /test-check-lists/:id} : get the "id" testCheckList.
     *
     * @param id the id of the testCheckList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the testCheckList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TestCheckList> getTestCheckList(@PathVariable("id") Long id) {
        log.debug("REST request to get TestCheckList : {}", id);
        Optional<TestCheckList> testCheckList = testCheckListService.findOne(id);
        return ResponseUtil.wrapOrNotFound(testCheckList);
    }

    /**
     * {@code DELETE  /test-check-lists/:id} : delete the "id" testCheckList.
     *
     * @param id the id of the testCheckList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTestCheckList(@PathVariable("id") Long id) {
        log.debug("REST request to delete TestCheckList : {}", id);
        testCheckListService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
