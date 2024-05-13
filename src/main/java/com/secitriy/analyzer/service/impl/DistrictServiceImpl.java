package com.secitriy.analyzer.service.impl;

import com.secitriy.analyzer.domain.District;
import com.secitriy.analyzer.repository.DistrictRepository;
import com.secitriy.analyzer.service.DistrictService;
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
 * Service Implementation for managing {@link com.secitriy.analyzer.domain.District}.
 */
@Service
@Transactional
public class DistrictServiceImpl implements DistrictService {

    private final Logger log = LoggerFactory.getLogger(DistrictServiceImpl.class);

    private final DistrictRepository districtRepository;

    public DistrictServiceImpl(DistrictRepository districtRepository) {
        this.districtRepository = districtRepository;
    }

    @Override
    public District save(District district) {
        log.debug("Request to save District : {}", district);
        return districtRepository.save(district);
    }

    @Override
    public District update(District district) {
        log.debug("Request to update District : {}", district);
        return districtRepository.save(district);
    }

    @Override
    public Optional<District> partialUpdate(District district) {
        log.debug("Request to partially update District : {}", district);

        return districtRepository
            .findById(district.getId())
            .map(existingDistrict -> {
                if (district.getName() != null) {
                    existingDistrict.setName(district.getName());
                }

                return existingDistrict;
            })
            .map(districtRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<District> findAll() {
        log.debug("Request to get all Districts");
        return districtRepository.findAll();
    }

    public Page<District> findAllWithEagerRelationships(Pageable pageable) {
        return districtRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the districts where UserAttributes is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<District> findAllWhereUserAttributesIsNull() {
        log.debug("Request to get all districts where UserAttributes is null");
        return StreamSupport.stream(districtRepository.findAll().spliterator(), false)
            .filter(district -> district.getUserAttributes() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<District> findOne(Long id) {
        log.debug("Request to get District : {}", id);
        return districtRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete District : {}", id);
        districtRepository.deleteById(id);
    }
}
