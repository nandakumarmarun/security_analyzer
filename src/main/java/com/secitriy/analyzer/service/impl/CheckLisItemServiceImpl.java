package com.secitriy.analyzer.service.impl;

import com.secitriy.analyzer.domain.CheckLisItem;
import com.secitriy.analyzer.repository.CheckLisItemRepository;
import com.secitriy.analyzer.service.CheckLisItemService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.secitriy.analyzer.domain.CheckLisItem}.
 */
@Service
@Transactional
public class CheckLisItemServiceImpl implements CheckLisItemService {

    private final Logger log = LoggerFactory.getLogger(CheckLisItemServiceImpl.class);

    private final CheckLisItemRepository checkLisItemRepository;

    public CheckLisItemServiceImpl(CheckLisItemRepository checkLisItemRepository) {
        this.checkLisItemRepository = checkLisItemRepository;
    }

    @Override
    public CheckLisItem save(CheckLisItem checkLisItem) {
        log.debug("Request to save CheckLisItem : {}", checkLisItem);
        return checkLisItemRepository.save(checkLisItem);
    }

    @Override
    public CheckLisItem update(CheckLisItem checkLisItem) {
        log.debug("Request to update CheckLisItem : {}", checkLisItem);
        return checkLisItemRepository.save(checkLisItem);
    }

    @Override
    public Optional<CheckLisItem> partialUpdate(CheckLisItem checkLisItem) {
        log.debug("Request to partially update CheckLisItem : {}", checkLisItem);

        return checkLisItemRepository
            .findById(checkLisItem.getId())
            .map(existingCheckLisItem -> {
                if (checkLisItem.getName() != null) {
                    existingCheckLisItem.setName(checkLisItem.getName());
                }
                if (checkLisItem.getValue() != null) {
                    existingCheckLisItem.setValue(checkLisItem.getValue());
                }

                return existingCheckLisItem;
            })
            .map(checkLisItemRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckLisItem> findAll() {
        log.debug("Request to get all CheckLisItems");
        return checkLisItemRepository.findAll();
    }

    /**
     *  Get all the checkLisItems where TestCheckLisItem is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CheckLisItem> findAllWhereTestCheckLisItemIsNull() {
        log.debug("Request to get all checkLisItems where TestCheckLisItem is null");
        return StreamSupport.stream(checkLisItemRepository.findAll().spliterator(), false)
            .filter(checkLisItem -> checkLisItem.getTestCheckLisItem() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CheckLisItem> findOne(Long id) {
        log.debug("Request to get CheckLisItem : {}", id);
        return checkLisItemRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CheckLisItem : {}", id);
        checkLisItemRepository.deleteById(id);
    }
}
