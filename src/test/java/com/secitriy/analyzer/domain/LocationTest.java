package com.secitriy.analyzer.domain;

import static com.secitriy.analyzer.domain.CityTestSamples.*;
import static com.secitriy.analyzer.domain.DistrictTestSamples.*;
import static com.secitriy.analyzer.domain.LocationTestSamples.*;
import static com.secitriy.analyzer.domain.StateTestSamples.*;
import static com.secitriy.analyzer.domain.UserAttributesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.secitriy.analyzer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Location.class);
        Location location1 = getLocationSample1();
        Location location2 = new Location();
        assertThat(location1).isNotEqualTo(location2);

        location2.setId(location1.getId());
        assertThat(location1).isEqualTo(location2);

        location2 = getLocationSample2();
        assertThat(location1).isNotEqualTo(location2);
    }

    @Test
    void stateTest() throws Exception {
        Location location = getLocationRandomSampleGenerator();
        State stateBack = getStateRandomSampleGenerator();

        location.setState(stateBack);
        assertThat(location.getState()).isEqualTo(stateBack);

        location.state(null);
        assertThat(location.getState()).isNull();
    }

    @Test
    void cityTest() throws Exception {
        Location location = getLocationRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        location.setCity(cityBack);
        assertThat(location.getCity()).isEqualTo(cityBack);

        location.city(null);
        assertThat(location.getCity()).isNull();
    }

    @Test
    void districtTest() throws Exception {
        Location location = getLocationRandomSampleGenerator();
        District districtBack = getDistrictRandomSampleGenerator();

        location.setDistrict(districtBack);
        assertThat(location.getDistrict()).isEqualTo(districtBack);

        location.district(null);
        assertThat(location.getDistrict()).isNull();
    }

    @Test
    void userAttributesTest() throws Exception {
        Location location = getLocationRandomSampleGenerator();
        UserAttributes userAttributesBack = getUserAttributesRandomSampleGenerator();

        location.setUserAttributes(userAttributesBack);
        assertThat(location.getUserAttributes()).isEqualTo(userAttributesBack);
        assertThat(userAttributesBack.getLocation()).isEqualTo(location);

        location.userAttributes(null);
        assertThat(location.getUserAttributes()).isNull();
        assertThat(userAttributesBack.getLocation()).isNull();
    }
}
