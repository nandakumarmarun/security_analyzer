package com.secitriy.analyzer.service.impl;

import com.secitriy.analyzer.domain.CheckList;
import com.secitriy.analyzer.repository.CheckListRepository;
import com.secitriy.analyzer.service.CheckListService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.secitriy.analyzer.domain.CheckList}.
 */
@Service
@Transactional
public class CheckListServiceImpl implements CheckListService {

    private final Logger log = LoggerFactory.getLogger(CheckListServiceImpl.class);

    private final CheckListRepository checkListRepository;

    public CheckListServiceImpl(CheckListRepository checkListRepository) {
        this.checkListRepository = checkListRepository;
    }

    @Override
    public CheckList save(CheckList checkList) {
        log.debug("Request to save CheckList : {}", checkList);
        return checkListRepository.save(checkList);
    }

    @Override
    public CheckList update(CheckList checkList) {
        log.debug("Request to update CheckList : {}", checkList);
        return checkListRepository.save(checkList);
    }

    @Override
    public Optional<CheckList> partialUpdate(CheckList checkList) {
        log.debug("Request to partially update CheckList : {}", checkList);

        return checkListRepository
            .findById(checkList.getId())
            .map(existingCheckList -> {
                if (checkList.getName() != null) {
                    existingCheckList.setName(checkList.getName());
                }

                return existingCheckList;
            })
            .map(checkListRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CheckList> findAll() {
        log.debug("Request to get all CheckLists");
        return checkListRepository.findAll();
    }

    /**
     *  Get all the checkLists where TestCheckList is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CheckList> findAllWhereTestCheckListIsNull() {
        log.debug("Request to get all checkLists where TestCheckList is null");
        return StreamSupport.stream(checkListRepository.findAll().spliterator(), false)
            .filter(checkList -> checkList.getTestCheckList() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CheckList> findOne(Long id) {
        log.debug("Request to get CheckList : {}", id);
        return checkListRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CheckList : {}", id);
        checkListRepository.deleteById(id);
    }
}
