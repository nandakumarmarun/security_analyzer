package com.secitriy.analyzer.domain;

import static com.secitriy.analyzer.domain.CityTestSamples.*;
import static com.secitriy.analyzer.domain.CountryTestSamples.*;
import static com.secitriy.analyzer.domain.LocationTestSamples.*;
import static com.secitriy.analyzer.domain.UserAttributesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.secitriy.analyzer.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(City.class);
        City city1 = getCitySample1();
        City city2 = new City();
        assertThat(city1).isNotEqualTo(city2);

        city2.setId(city1.getId());
        assertThat(city1).isEqualTo(city2);

        city2 = getCitySample2();
        assertThat(city1).isNotEqualTo(city2);
    }

    @Test
    void locationTest() throws Exception {
        City city = getCityRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        city.addLocation(locationBack);
        assertThat(city.getLocations()).containsOnly(locationBack);
        assertThat(locationBack.getCity()).isEqualTo(city);

        city.removeLocation(locationBack);
        assertThat(city.getLocations()).doesNotContain(locationBack);
        assertThat(locationBack.getCity()).isNull();

        city.locations(new HashSet<>(Set.of(locationBack)));
        assertThat(city.getLocations()).containsOnly(locationBack);
        assertThat(locationBack.getCity()).isEqualTo(city);

        city.setLocations(new HashSet<>());
        assertThat(city.getLocations()).doesNotContain(locationBack);
        assertThat(locationBack.getCity()).isNull();
    }

    @Test
    void countryTest() throws Exception {
        City city = getCityRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        city.setCountry(countryBack);
        assertThat(city.getCountry()).isEqualTo(countryBack);

        city.country(null);
        assertThat(city.getCountry()).isNull();
    }

    @Test
    void userAttributesTest() throws Exception {
        City city = getCityRandomSampleGenerator();
        UserAttributes userAttributesBack = getUserAttributesRandomSampleGenerator();

        city.setUserAttributes(userAttributesBack);
        assertThat(city.getUserAttributes()).isEqualTo(userAttributesBack);
        assertThat(userAttributesBack.getCity()).isEqualTo(city);

        city.userAttributes(null);
        assertThat(city.getUserAttributes()).isNull();
        assertThat(userAttributesBack.getCity()).isNull();
    }
}
