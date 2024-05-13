package com.secitriy.analyzer.domain;

import static com.secitriy.analyzer.domain.CityTestSamples.*;
import static com.secitriy.analyzer.domain.CountryTestSamples.*;
import static com.secitriy.analyzer.domain.StateTestSamples.*;
import static com.secitriy.analyzer.domain.UserAttributesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.secitriy.analyzer.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CountryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Country.class);
        Country country1 = getCountrySample1();
        Country country2 = new Country();
        assertThat(country1).isNotEqualTo(country2);

        country2.setId(country1.getId());
        assertThat(country1).isEqualTo(country2);

        country2 = getCountrySample2();
        assertThat(country1).isNotEqualTo(country2);
    }

    @Test
    void stateTest() throws Exception {
        Country country = getCountryRandomSampleGenerator();
        State stateBack = getStateRandomSampleGenerator();

        country.addState(stateBack);
        assertThat(country.getStates()).containsOnly(stateBack);
        assertThat(stateBack.getCountry()).isEqualTo(country);

        country.removeState(stateBack);
        assertThat(country.getStates()).doesNotContain(stateBack);
        assertThat(stateBack.getCountry()).isNull();

        country.states(new HashSet<>(Set.of(stateBack)));
        assertThat(country.getStates()).containsOnly(stateBack);
        assertThat(stateBack.getCountry()).isEqualTo(country);

        country.setStates(new HashSet<>());
        assertThat(country.getStates()).doesNotContain(stateBack);
        assertThat(stateBack.getCountry()).isNull();
    }

    @Test
    void cityTest() throws Exception {
        Country country = getCountryRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        country.addCity(cityBack);
        assertThat(country.getCities()).containsOnly(cityBack);
        assertThat(cityBack.getCountry()).isEqualTo(country);

        country.removeCity(cityBack);
        assertThat(country.getCities()).doesNotContain(cityBack);
        assertThat(cityBack.getCountry()).isNull();

        country.cities(new HashSet<>(Set.of(cityBack)));
        assertThat(country.getCities()).containsOnly(cityBack);
        assertThat(cityBack.getCountry()).isEqualTo(country);

        country.setCities(new HashSet<>());
        assertThat(country.getCities()).doesNotContain(cityBack);
        assertThat(cityBack.getCountry()).isNull();
    }

    @Test
    void userAttributesTest() throws Exception {
        Country country = getCountryRandomSampleGenerator();
        UserAttributes userAttributesBack = getUserAttributesRandomSampleGenerator();

        country.setUserAttributes(userAttributesBack);
        assertThat(country.getUserAttributes()).isEqualTo(userAttributesBack);
        assertThat(userAttributesBack.getCountry()).isEqualTo(country);

        country.userAttributes(null);
        assertThat(country.getUserAttributes()).isNull();
        assertThat(userAttributesBack.getCountry()).isNull();
    }
}
