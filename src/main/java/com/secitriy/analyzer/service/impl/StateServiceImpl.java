package com.secitriy.analyzer.service.impl;

import com.secitriy.analyzer.domain.State;
import com.secitriy.analyzer.repository.StateRepository;
import com.secitriy.analyzer.service.StateService;
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
 * Service Implementation for managing {@link com.secitriy.analyzer.domain.State}.
 */
@Service
@Transactional
public class StateServiceImpl implements StateService {

    private final Logger log = LoggerFactory.getLogger(StateServiceImpl.class);

    private final StateRepository stateRepository;

    public StateServiceImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public State save(State state) {
        log.debug("Request to save State : {}", state);
        return stateRepository.save(state);
    }

    @Override
    public State update(State state) {
        log.debug("Request to update State : {}", state);
        return stateRepository.save(state);
    }

    @Override
    public Optional<State> partialUpdate(State state) {
        log.debug("Request to partially update State : {}", state);

        return stateRepository
            .findById(state.getId())
            .map(existingState -> {
                if (state.getName() != null) {
                    existingState.setName(state.getName());
                }

                return existingState;
            })
            .map(stateRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<State> findAll() {
        log.debug("Request to get all States");
        return stateRepository.findAll();
    }

    public Page<State> findAllWithEagerRelationships(Pageable pageable) {
        return stateRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the states where UserAttributes is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<State> findAllWhereUserAttributesIsNull() {
        log.debug("Request to get all states where UserAttributes is null");
        return StreamSupport.stream(stateRepository.findAll().spliterator(), false)
            .filter(state -> state.getUserAttributes() == null)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<State> findOne(Long id) {
        log.debug("Request to get State : {}", id);
        return stateRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete State : {}", id);
        stateRepository.deleteById(id);
    }
}
