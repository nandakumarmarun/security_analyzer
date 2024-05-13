package com.secitriy.analyzer.service.impl;

import com.secitriy.analyzer.domain.TestCheckList;
import com.secitriy.analyzer.repository.TestCheckListRepository;
import com.secitriy.analyzer.service.TestCheckListService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.secitriy.analyzer.domain.TestCheckList}.
 */
@Service
@Transactional
public class TestCheckListServiceImpl implements TestCheckListService {

    private final Logger log = LoggerFactory.getLogger(TestCheckListServiceImpl.class);

    private final TestCheckListRepository testCheckListRepository;

    public TestCheckListServiceImpl(TestCheckListRepository testCheckListRepository) {
        this.testCheckListRepository = testCheckListRepository;
    }

    @Override
    public TestCheckList save(TestCheckList testCheckList) {
        log.debug("Request to save TestCheckList : {}", testCheckList);
        return testCheckListRepository.save(testCheckList);
    }

    @Override
    public TestCheckList update(TestCheckList testCheckList) {
        log.debug("Request to update TestCheckList : {}", testCheckList);
        return testCheckListRepository.save(testCheckList);
    }

    @Override
    public Optional<TestCheckList> partialUpdate(TestCheckList testCheckList) {
        log.debug("Request to partially update TestCheckList : {}", testCheckList);

        return testCheckListRepository.findById(testCheckList.getId()).map(testCheckListRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestCheckList> findAll() {
        log.debug("Request to get all TestCheckLists");
        return testCheckListRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TestCheckList> findOne(Long id) {
        log.debug("Request to get TestCheckList : {}", id);
        return testCheckListRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TestCheckList : {}", id);
        testCheckListRepository.deleteById(id);
    }
}
