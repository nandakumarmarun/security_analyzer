package com.secitriy.analyzer.service.impl;

import com.secitriy.analyzer.domain.TestCheckLisItem;
import com.secitriy.analyzer.repository.TestCheckLisItemRepository;
import com.secitriy.analyzer.service.TestCheckLisItemService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.secitriy.analyzer.domain.TestCheckLisItem}.
 */
@Service
@Transactional
public class TestCheckLisItemServiceImpl implements TestCheckLisItemService {

    private final Logger log = LoggerFactory.getLogger(TestCheckLisItemServiceImpl.class);

    private final TestCheckLisItemRepository testCheckLisItemRepository;

    public TestCheckLisItemServiceImpl(TestCheckLisItemRepository testCheckLisItemRepository) {
        this.testCheckLisItemRepository = testCheckLisItemRepository;
    }

    @Override
    public TestCheckLisItem save(TestCheckLisItem testCheckLisItem) {
        log.debug("Request to save TestCheckLisItem : {}", testCheckLisItem);
        return testCheckLisItemRepository.save(testCheckLisItem);
    }

    @Override
    public TestCheckLisItem update(TestCheckLisItem testCheckLisItem) {
        log.debug("Request to update TestCheckLisItem : {}", testCheckLisItem);
        return testCheckLisItemRepository.save(testCheckLisItem);
    }

    @Override
    public Optional<TestCheckLisItem> partialUpdate(TestCheckLisItem testCheckLisItem) {
        log.debug("Request to partially update TestCheckLisItem : {}", testCheckLisItem);

        return testCheckLisItemRepository
            .findById(testCheckLisItem.getId())
            .map(existingTestCheckLisItem -> {
                if (testCheckLisItem.getMarked() != null) {
                    existingTestCheckLisItem.setMarked(testCheckLisItem.getMarked());
                }

                return existingTestCheckLisItem;
            })
            .map(testCheckLisItemRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TestCheckLisItem> findAll() {
        log.debug("Request to get all TestCheckLisItems");
        return testCheckLisItemRepository.findAll();
    }

    public Page<TestCheckLisItem> findAllWithEagerRelationships(Pageable pageable) {
        return testCheckLisItemRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TestCheckLisItem> findOne(Long id) {
        log.debug("Request to get TestCheckLisItem : {}", id);
        return testCheckLisItemRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TestCheckLisItem : {}", id);
        testCheckLisItemRepository.deleteById(id);
    }
}
