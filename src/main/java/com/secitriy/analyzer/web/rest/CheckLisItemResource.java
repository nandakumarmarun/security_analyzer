package com.secitriy.analyzer.web.rest;

import com.secitriy.analyzer.domain.CheckLisItem;
import com.secitriy.analyzer.repository.CheckLisItemRepository;
import com.secitriy.analyzer.service.CheckLisItemService;
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
 * REST controller for managing {@link com.secitriy.analyzer.domain.CheckLisItem}.
 */
@RestController
@RequestMapping("/api/check-lis-items")
public class CheckLisItemResource {

    private final Logger log = LoggerFactory.getLogger(CheckLisItemResource.class);

    private static final String ENTITY_NAME = "checkLisItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CheckLisItemService checkLisItemService;

    private final CheckLisItemRepository checkLisItemRepository;

    public CheckLisItemResource(CheckLisItemService checkLisItemService, CheckLisItemRepository checkLisItemRepository) {
        this.checkLisItemService = checkLisItemService;
        this.checkLisItemRepository = checkLisItemRepository;
    }

    /**
     * {@code POST  /check-lis-items} : Create a new checkLisItem.
     *
     * @param checkLisItem the checkLisItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new checkLisItem, or with status {@code 400 (Bad Request)} if the checkLisItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CheckLisItem> createCheckLisItem(@RequestBody CheckLisItem checkLisItem) throws URISyntaxException {
        log.debug("REST request to save CheckLisItem : {}", checkLisItem);
        if (checkLisItem.getId() != null) {
            throw new BadRequestAlertException("A new checkLisItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        checkLisItem = checkLisItemService.save(checkLisItem);
        return ResponseEntity.created(new URI("/api/check-lis-items/" + checkLisItem.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, checkLisItem.getId().toString()))
            .body(checkLisItem);
    }

    /**
     * {@code PUT  /check-lis-items/:id} : Updates an existing checkLisItem.
     *
     * @param id the id of the checkLisItem to save.
     * @param checkLisItem the checkLisItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated checkLisItem,
     * or with status {@code 400 (Bad Request)} if the checkLisItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the checkLisItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CheckLisItem> updateCheckLisItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CheckLisItem checkLisItem
    ) throws URISyntaxException {
        log.debug("REST request to update CheckLisItem : {}, {}", id, checkLisItem);
        if (checkLisItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, checkLisItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!checkLisItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        checkLisItem = checkLisItemService.update(checkLisItem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, checkLisItem.getId().toString()))
            .body(checkLisItem);
    }

    /**
     * {@code PATCH  /check-lis-items/:id} : Partial updates given fields of an existing checkLisItem, field will ignore if it is null
     *
     * @param id the id of the checkLisItem to save.
     * @param checkLisItem the checkLisItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated checkLisItem,
     * or with status {@code 400 (Bad Request)} if the checkLisItem is not valid,
     * or with status {@code 404 (Not Found)} if the checkLisItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the checkLisItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CheckLisItem> partialUpdateCheckLisItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CheckLisItem checkLisItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update CheckLisItem partially : {}, {}", id, checkLisItem);
        if (checkLisItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, checkLisItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!checkLisItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CheckLisItem> result = checkLisItemService.partialUpdate(checkLisItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, checkLisItem.getId().toString())
        );
    }

    /**
     * {@code GET  /check-lis-items} : get all the checkLisItems.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of checkLisItems in body.
     */
    @GetMapping("")
    public List<CheckLisItem> getAllCheckLisItems(@RequestParam(name = "filter", required = false) String filter) {
        if ("testchecklisitem-is-null".equals(filter)) {
            log.debug("REST request to get all CheckLisItems where testCheckLisItem is null");
            return checkLisItemService.findAllWhereTestCheckLisItemIsNull();
        }
        log.debug("REST request to get all CheckLisItems");
        return checkLisItemService.findAll();
    }

    /**
     * {@code GET  /check-lis-items/:id} : get the "id" checkLisItem.
     *
     * @param id the id of the checkLisItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the checkLisItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CheckLisItem> getCheckLisItem(@PathVariable("id") Long id) {
        log.debug("REST request to get CheckLisItem : {}", id);
        Optional<CheckLisItem> checkLisItem = checkLisItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(checkLisItem);
    }

    /**
     * {@code DELETE  /check-lis-items/:id} : delete the "id" checkLisItem.
     *
     * @param id the id of the checkLisItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCheckLisItem(@PathVariable("id") Long id) {
        log.debug("REST request to delete CheckLisItem : {}", id);
        checkLisItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
