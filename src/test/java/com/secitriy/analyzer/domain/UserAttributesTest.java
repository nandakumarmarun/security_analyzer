package com.secitriy.analyzer.domain;

import static com.secitriy.analyzer.domain.ApplicationUserTestSamples.*;
import static com.secitriy.analyzer.domain.CityTestSamples.*;
import static com.secitriy.analyzer.domain.CountryTestSamples.*;
import static com.secitriy.analyzer.domain.DistrictTestSamples.*;
import static com.secitriy.analyzer.domain.LocationTestSamples.*;
import static com.secitriy.analyzer.domain.StateTestSamples.*;
import static com.secitriy.analyzer.domain.UserAttributesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.secitriy.analyzer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserAttributesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserAttributes.class);
        UserAttributes userAttributes1 = getUserAttributesSample1();
        UserAttributes userAttributes2 = new UserAttributes();
        assertThat(userAttributes1).isNotEqualTo(userAttributes2);

        userAttributes2.setId(userAttributes1.getId());
        assertThat(userAttributes1).isEqualTo(userAttributes2);

        userAttributes2 = getUserAttributesSample2();
        assertThat(userAttributes1).isNotEqualTo(userAttributes2);
    }

    @Test
    void countryTest() throws Exception {
        UserAttributes userAttributes = getUserAttributesRandomSampleGenerator();
        Country countryBack = getCountryRandomSampleGenerator();

        userAttributes.setCountry(countryBack);
        assertThat(userAttributes.getCountry()).isEqualTo(countryBack);

        userAttributes.country(null);
        assertThat(userAttributes.getCountry()).isNull();
    }

    @Test
    void stateTest() throws Exception {
        UserAttributes userAttributes = getUserAttributesRandomSampleGenerator();
        State stateBack = getStateRandomSampleGenerator();

        userAttributes.setState(stateBack);
        assertThat(userAttributes.getState()).isEqualTo(stateBack);

        userAttributes.state(null);
        assertThat(userAttributes.getState()).isNull();
    }

    @Test
    void districtTest() throws Exception {
        UserAttributes userAttributes = getUserAttributesRandomSampleGenerator();
        District districtBack = getDistrictRandomSampleGenerator();

        userAttributes.setDistrict(districtBack);
        assertThat(userAttributes.getDistrict()).isEqualTo(districtBack);

        userAttributes.district(null);
        assertThat(userAttributes.getDistrict()).isNull();
    }

    @Test
    void cityTest() throws Exception {
        UserAttributes userAttributes = getUserAttributesRandomSampleGenerator();
        City cityBack = getCityRandomSampleGenerator();

        userAttributes.setCity(cityBack);
        assertThat(userAttributes.getCity()).isEqualTo(cityBack);

        userAttributes.city(null);
        assertThat(userAttributes.getCity()).isNull();
    }

    @Test
    void locationTest() throws Exception {
        UserAttributes userAttributes = getUserAttributesRandomSampleGenerator();
        Location locationBack = getLocationRandomSampleGenerator();

        userAttributes.setLocation(locationBack);
        assertThat(userAttributes.getLocation()).isEqualTo(locationBack);

        userAttributes.location(null);
        assertThat(userAttributes.getLocation()).isNull();
    }

    @Test
    void applicationUserTest() throws Exception {
        UserAttributes userAttributes = getUserAttributesRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        userAttributes.setApplicationUser(applicationUserBack);
        assertThat(userAttributes.getApplicationUser()).isEqualTo(applicationUserBack);
        assertThat(applicationUserBack.getUserAttributes()).isEqualTo(userAttributes);

        userAttributes.applicationUser(null);
        assertThat(userAttributes.getApplicationUser()).isNull();
        assertThat(applicationUserBack.getUserAttributes()).isNull();
    }
}
