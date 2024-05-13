package com.secitriy.analyzer.service.impl;

import com.secitriy.analyzer.domain.UserAttributes;
import com.secitriy.analyzer.repository.UserAttributesRepository;
import com.secitriy.analyzer.service.UserAttributesService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.secitriy.analyzer.domain.UserAttributes}.
 */
@Service
@Transactional
public class UserAttributesServiceImpl implements UserAttributesService {

    private final Logger log = LoggerFactory.getLogger(UserAttributesServiceImpl.class);

    private final UserAttributesRepository userAttributesRepository;

    public UserAttributesServiceImpl(UserAttributesRepository userAttributesRepository) {
        this.userAttributesRepository = userAttributesRepository;
    }

    @Override
    public UserAttributes save(UserAttributes userAttributes) {
        log.debug("Request to save UserAttributes : {}", userAttributes);
        return userAttributesRepository.save(userAttributes);
    }

    @Override
    public UserAttributes update(UserAttributes userAttributes) {
        log.debug("Request to update UserAttributes : {}", userAttributes);
        return userAttributesRepository.save(userAttributes);
    }

    @Override
    public Optional<UserAttributes> partialUpdate(UserAttributes userAttributes) {
        log.debug("Request to partially update UserAttributes : {}", userAttributes);

        return userAttributesRepository
            .findById(userAttributes.getId())
            .map(existingUserAttributes -> {
                if (userAttributes.getName() != null) {
                    existingUserAttributes.setName(userAttributes.getName());
                }
                if (userAttributes.getPhone() != null) {
                    existingUserAttributes.setPhone(userAttributes.getPhone());
                }
                if (userAttributes.getEmail() != null) {
                    existingUserAttributes.setEmail(userAttributes.getEmail());
                }
                if (userAttributes.getAddress() != null) {
                    existingUserAttributes.setAddress(userAttributes.getAddress());
                }

                return existingUserAttributes;
            })
            .map(userAttributesRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserAttributes> findAll() {
        log.debug("Request to get all UserAttributes");
        return userAttributesRepository.findAll();
    }

    public Page<UserAttributes> findAllWithEagerRelationships(Pageable pageable) {
        return userAttributesRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the userAttributes where ApplicationUser is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<UserAttributes> findAllWhereApplicationUserIsNull() {
        log.debug("Request to get all userAttributes where ApplicationUser is null");
        return StreamSupport.stream(userAttributesRepository.findAll().spliterator(), false)
            .filter(userAttributes -> userAttributes.getApplicationUser() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserAttributes> findOne(Long id) {
        log.debug("Request to get UserAttributes : {}", id);
        return userAttributesRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete UserAttributes : {}", id);
        userAttributesRepository.deleteById(id);
    }
}
