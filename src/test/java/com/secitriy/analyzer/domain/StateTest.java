package com.secitriy.analyzer.domain;

import static com.secitriy.analyzer.domain.CountryTestSamples.*;
import static com.secitriy.analyzer.domain.DistrictTestSamples.*;
import static com.secitriy.analyzer.domain.LocationTestSamples.*;
import static com.secitriy.analyzer.domain.StateTestSamples.*;
import static com.secitriy.analyzer.domain.UserAttributesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.secitriy.analyzer.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(State.class);
        State state1 = getStateSample1();
        State state2 = new State();
        assertThat(state1).isNotEqualTo(state2);

        state2.setId(state1.getId());
        assertThat(state1).isEqualTo(state2);

        state2 = getStateSample2();
        assertThat(state1).isNotEqualTo(state2);
    }

    @Test
    void locationTest() throws Exception {
        State state = getStateRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        state.addLocation(locationBack);
        assertThat(state.getLocations()).containsOnly(locationBack);
        assertThat(locationBack.getState()).isEqualTo(state);

        state.removeLocation(locationBack);
        assertThat(state.getLocations()).doesNotContain(locationBack);
        assertThat(locationBack.getState()).isNull();

        state.locations(new HashSet<>(Set.of(locationBack)));
        assertThat(state.getLocations()).containsOnly(locationBack);
        assertThat(locationBack.getState()).isEqualTo(state);

        state.setLocations(new HashSet<>());
        assertThat(state.getLocations()).doesNotContain(locationBack);
        assertThat(locationBack.getState()).isNull();
    }

    @Test
    void districtTest() throws Exception {
        State state = getStateRandomSampleGenerator();
        District districtBack = getDistrictRandomSampleGenerator();

        state.addDistrict(districtBack);
        assertThat(state.getDistricts()).containsOnly(districtBack);
        assertThat(districtBack.getState()).isEqualTo(state);

        state.removeDistrict(districtBack);
        assertThat(state.getDistricts()).doesNotContain(districtBack);
        assertThat(districtBack.getState()).isNull();

        state.districts(new HashSet<>(Set.of(districtBack)));
        assertThat(state.getDistricts()).containsOnly(districtBack);
        assertThat(districtBack.getState()).isEqualTo(state);

        state.setDistricts(new HashSet<>());
        assertThat(state.getDistricts()).doesNotContain(districtBack);
        assertThat(districtBack.getState()).isNull();
    }

    @Test
    void countryTest() throws Exception {
        State state = getStateRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        state.setCountry(countryBack);
        assertThat(state.getCountry()).isEqualTo(countryBack);

        state.country(null);
        assertThat(state.getCountry()).isNull();
    }

    @Test
    void userAttributesTest() throws Exception {
        State state = getStateRandomSampleGenerator();
        UserAttributes userAttributesBack = getUserAttributesRandomSampleGenerator();

        state.setUserAttributes(userAttributesBack);
        assertThat(state.getUserAttributes()).isEqualTo(userAttributesBack);
        assertThat(userAttributesBack.getState()).isEqualTo(state);

        state.userAttributes(null);
        assertThat(state.getUserAttributes()).isNull();
        assertThat(userAttributesBack.getState()).isNull();
    }
}
