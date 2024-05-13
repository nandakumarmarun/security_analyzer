package com.secitriy.analyzer.web.rest;

import com.secitriy.analyzer.domain.SecurityTest;
import com.secitriy.analyzer.repository.SecurityTestRepository;
import com.secitriy.analyzer.service.SecurityTestService;
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
 * REST controller for managing {@link com.secitriy.analyzer.domain.SecurityTest}.
 */
@RestController
@RequestMapping("/api/security-tests")
public class SecurityTestResource {

    private final Logger log = LoggerFactory.getLogger(SecurityTestResource.class);

    private static final String ENTITY_NAME = "securityTest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SecurityTestService securityTestService;

    private final SecurityTestRepository securityTestRepository;

    public SecurityTestResource(SecurityTestService securityTestService, SecurityTestRepository securityTestRepository) {
        this.securityTestService = securityTestService;
        this.securityTestRepository = securityTestRepository;
    }

    /**
     * {@code POST  /security-tests} : Create a new securityTest.
     *
     * @param securityTest the securityTest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new securityTest, or with status {@code 400 (Bad Request)} if the securityTest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SecurityTest> createSecurityTest(@RequestBody SecurityTest securityTest) throws URISyntaxException {
        log.debug("REST request to save SecurityTest : {}", securityTest);
        if (securityTest.getId() != null) {
            throw new BadRequestAlertException("A new securityTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        securityTest = securityTestService.save(securityTest);
        return ResponseEntity.created(new URI("/api/security-tests/" + securityTest.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, securityTest.getId().toString()))
            .body(securityTest);
    }

    /**
     * {@code PUT  /security-tests/:id} : Updates an existing securityTest.
     *
     * @param id the id of the securityTest to save.
     * @param securityTest the securityTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated securityTest,
     * or with status {@code 400 (Bad Request)} if the securityTest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the securityTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SecurityTest> updateSecurityTest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SecurityTest securityTest
    ) throws URISyntaxException {
        log.debug("REST request to update SecurityTest : {}, {}", id, securityTest);
        if (securityTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, securityTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!securityTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        securityTest = securityTestService.update(securityTest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, securityTest.getId().toString()))
            .body(securityTest);
    }

    /**
     * {@code PATCH  /security-tests/:id} : Partial updates given fields of an existing securityTest, field will ignore if it is null
     *
     * @param id the id of the securityTest to save.
     * @param securityTest the securityTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated securityTest,
     * or with status {@code 400 (Bad Request)} if the securityTest is not valid,
     * or with status {@code 404 (Not Found)} if the securityTest is not found,
     * or with status {@code 500 (Internal Server Error)} if the securityTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SecurityTest> partialUpdateSecurityTest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SecurityTest securityTest
    ) throws URISyntaxException {
        log.debug("REST request to partial update SecurityTest partially : {}, {}", id, securityTest);
        if (securityTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, securityTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!securityTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SecurityTest> result = securityTestService.partialUpdate(securityTest);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, securityTest.getId().toString())
        );
    }

    /**
     * {@code GET  /security-tests} : get all the securityTests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of securityTests in body.
     */
    @GetMapping("")
    public List<SecurityTest> getAllSecurityTests() {
        log.debug("REST request to get all SecurityTests");
        return securityTestService.findAll();
    }

    /**
     * {@code GET  /security-tests/:id} : get the "id" securityTest.
     *
     * @param id the id of the securityTest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the securityTest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SecurityTest> getSecurityTest(@PathVariable("id") Long id) {
        log.debug("REST request to get SecurityTest : {}", id);
        Optional<SecurityTest> securityTest = securityTestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(securityTest);
    }

    /**
     * {@code DELETE  /security-tests/:id} : delete the "id" securityTest.
     *
     * @param id the id of the securityTest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSecurityTest(@PathVariable("id") Long id) {
        log.debug("REST request to delete SecurityTest : {}", id);
        securityTestService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
