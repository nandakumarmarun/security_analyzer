package com.secitriy.analyzer.domain;

import static com.secitriy.analyzer.domain.DistrictTestSamples.*;
import static com.secitriy.analyzer.domain.LocationTestSamples.*;
import static com.secitriy.analyzer.domain.StateTestSamples.*;
import static com.secitriy.analyzer.domain.UserAttributesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.secitriy.analyzer.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DistrictTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(District.class);
        District district1 = getDistrictSample1();
        District district2 = new District();
        assertThat(district1).isNotEqualTo(district2);

        district2.setId(district1.getId());
        assertThat(district1).isEqualTo(district2);

        district2 = getDistrictSample2();
        assertThat(district1).isNotEqualTo(district2);
    }

    @Test
    void locationTest() throws Exception {
        District district = getDistrictRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        district.addLocation(locationBack);
        assertThat(district.getLocations()).containsOnly(locationBack);
        assertThat(locationBack.getDistrict()).isEqualTo(district);

        district.removeLocation(locationBack);
        assertThat(district.getLocations()).doesNotContain(locationBack);
        assertThat(locationBack.getDistrict()).isNull();

        district.locations(new HashSet<>(Set.of(locationBack)));
        assertThat(district.getLocations()).containsOnly(locationBack);
        assertThat(locationBack.getDistrict()).isEqualTo(district);

        district.setLocations(new HashSet<>());
        assertThat(district.getLocations()).doesNotContain(locationBack);
        assertThat(locationBack.getDistrict()).isNull();
    }

    @Test
    void stateTest() throws Exception {
        District district = getDistrictRandomSampleGenerator();
        State stateBack = getStateRandomSampleGenerator();

        district.setState(stateBack);
        assertThat(district.getState()).isEqualTo(stateBack);

        district.state(null);
        assertThat(district.getState()).isNull();
    }

    @Test
    void userAttributesTest() throws Exception {
        District district = getDistrictRandomSampleGenerator();
        UserAttributes userAttributesBack = getUserAttributesRandomSampleGenerator();

        district.setUserAttributes(userAttributesBack);
        assertThat(district.getUserAttributes()).isEqualTo(userAttributesBack);
        assertThat(userAttributesBack.getDistrict()).isEqualTo(district);

        district.userAttributes(null);
        assertThat(district.getUserAttributes()).isNull();
        assertThat(userAttributesBack.getDistrict()).isNull();
    }
}
