package com.secitriy.analyzer.service.impl;

import com.secitriy.analyzer.domain.SecurityTest;
import com.secitriy.analyzer.repository.SecurityTestRepository;
import com.secitriy.analyzer.service.SecurityTestService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.secitriy.analyzer.domain.SecurityTest}.
 */
@Service
@Transactional
public class SecurityTestServiceImpl implements SecurityTestService {

    private final Logger log = LoggerFactory.getLogger(SecurityTestServiceImpl.class);

    private final SecurityTestRepository securityTestRepository;

    public SecurityTestServiceImpl(SecurityTestRepository securityTestRepository) {
        this.securityTestRepository = securityTestRepository;
    }

    @Override
    public SecurityTest save(SecurityTest securityTest) {
        log.debug("Request to save SecurityTest : {}", securityTest);
        return securityTestRepository.save(securityTest);
    }

    @Override
    public SecurityTest update(SecurityTest securityTest) {
        log.debug("Request to update SecurityTest : {}", securityTest);
        return securityTestRepository.save(securityTest);
    }

    @Override
    public Optional<SecurityTest> partialUpdate(SecurityTest securityTest) {
        log.debug("Request to partially update SecurityTest : {}", securityTest);

        return securityTestRepository
            .findById(securityTest.getId())
            .map(existingSecurityTest -> {
                if (securityTest.getTestStatus() != null) {
                    existingSecurityTest.setTestStatus(securityTest.getTestStatus());
                }
                if (securityTest.getTestScore() != null) {
                    existingSecurityTest.setTestScore(securityTest.getTestScore());
                }
                if (securityTest.getSecurityLevel() != null) {
                    existingSecurityTest.setSecurityLevel(securityTest.getSecurityLevel());
                }

                return existingSecurityTest;
            })
            .map(securityTestRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SecurityTest> findAll() {
        log.debug("Request to get all SecurityTests");
        return securityTestRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SecurityTest> findOne(Long id) {
        log.debug("Request to get SecurityTest : {}", id);
        return securityTestRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SecurityTest : {}", id);
        securityTestRepository.deleteById(id);
    }
}
