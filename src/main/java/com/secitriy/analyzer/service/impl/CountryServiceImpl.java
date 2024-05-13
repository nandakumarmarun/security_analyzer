package com.secitriy.analyzer.service.impl;

import com.secitriy.analyzer.domain.Country;
import com.secitriy.analyzer.repository.CountryRepository;
import com.secitriy.analyzer.service.CountryService;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.secitriy.analyzer.domain.Country}.
 */
@Service
@Transactional
public class CountryServiceImpl implements CountryService {

    private final Logger log = LoggerFactory.getLogger(CountryServiceImpl.class);

    private final CountryRepository countryRepository;

    public CountryServiceImpl(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    @Override
    public Country save(Country country) {
        log.debug("Request to save Country : {}", country);
        return countryRepository.save(country);
    }

    @Override
    public Country update(Country country) {
        log.debug("Request to update Country : {}", country);
        return countryRepository.save(country);
    }

    @Override
    public Optional<Country> partialUpdate(Country country) {
        log.debug("Request to partially update Country : {}", country);

        return countryRepository
            .findById(country.getId())
            .map(existingCountry -> {
                if (country.getName() != null) {
                    existingCountry.setName(country.getName());
                }

                return existingCountry;
            })
            .map(countryRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Country> findAll() {
        log.debug("Request to get all Countries");
        return countryRepository.findAll();
    }

    /**
     *  Get all the countries where UserAttributes is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Country> findAllWhereUserAttributesIsNull() {
        log.debug("Request to get all countries where UserAttributes is null");
        return StreamSupport.stream(countryRepository.findAll().spliterator(), false)
            .filter(country -> country.getUserAttributes() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Country> findOne(Long id) {
        log.debug("Request to get Country : {}", id);
        return countryRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Country : {}", id);
        countryRepository.deleteById(id);
    }
}
