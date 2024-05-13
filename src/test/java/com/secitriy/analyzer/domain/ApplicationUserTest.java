package com.secitriy.analyzer.domain;

import static com.secitriy.analyzer.domain.ApplicationUserTestSamples.*;
import static com.secitriy.analyzer.domain.SecurityTestTestSamples.*;
import static com.secitriy.analyzer.domain.UserAttributesTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.secitriy.analyzer.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ApplicationUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationUser.class);
        ApplicationUser applicationUser1 = getApplicationUserSample1();
        ApplicationUser applicationUser2 = new ApplicationUser();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);

        applicationUser2.setId(applicationUser1.getId());
        assertThat(applicationUser1).isEqualTo(applicationUser2);

        applicationUser2 = getApplicationUserSample2();
        assertThat(applicationUser1).isNotEqualTo(applicationUser2);
    }

    @Test
    void userAttributesTest() throws Exception {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        UserAttributes userAttributesBack = getUserAttributesRandomSampleGenerator();

        applicationUser.setUserAttributes(userAttributesBack);
        assertThat(applicationUser.getUserAttributes()).isEqualTo(userAttributesBack);

        applicationUser.userAttributes(null);
        assertThat(applicationUser.getUserAttributes()).isNull();
    }

    @Test
    void securityTestTest() throws Exception {
        ApplicationUser applicationUser = getApplicationUserRandomSampleGenerator();
        SecurityTest securityTestBack = getSecurityTestRandomSampleGenerator();

        applicationUser.addSecurityTest(securityTestBack);
        assertThat(applicationUser.getSecurityTests()).containsOnly(securityTestBack);
        assertThat(securityTestBack.getApplicationUser()).isEqualTo(applicationUser);

        applicationUser.removeSecurityTest(securityTestBack);
        assertThat(applicationUser.getSecurityTests()).doesNotContain(securityTestBack);
        assertThat(securityTestBack.getApplicationUser()).isNull();

        applicationUser.securityTests(new HashSet<>(Set.of(securityTestBack)));
        assertThat(applicationUser.getSecurityTests()).containsOnly(securityTestBack);
        assertThat(securityTestBack.getApplicationUser()).isEqualTo(applicationUser);

        applicationUser.setSecurityTests(new HashSet<>());
        assertThat(applicationUser.getSecurityTests()).doesNotContain(securityTestBack);
        assertThat(securityTestBack.getApplicationUser()).isNull();
    }
}
