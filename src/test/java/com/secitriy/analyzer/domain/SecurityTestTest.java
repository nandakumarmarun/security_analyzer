package com.secitriy.analyzer.domain;

import static com.secitriy.analyzer.domain.ApplicationUserTestSamples.*;
import static com.secitriy.analyzer.domain.SecurityTestTestSamples.*;
import static com.secitriy.analyzer.domain.TestCheckListTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.secitriy.analyzer.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SecurityTestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SecurityTest.class);
        SecurityTest securityTest1 = getSecurityTestSample1();
        SecurityTest securityTest2 = new SecurityTest();
        assertThat(securityTest1).isNotEqualTo(securityTest2);

        securityTest2.setId(securityTest1.getId());
        assertThat(securityTest1).isEqualTo(securityTest2);

        securityTest2 = getSecurityTestSample2();
        assertThat(securityTest1).isNotEqualTo(securityTest2);
    }

    @Test
    void testCheckListTest() throws Exception {
        SecurityTest securityTest = getSecurityTestRandomSampleGenerator();
        TestCheckList testCheckListBack = getTestCheckListRandomSampleGenerator();

        securityTest.addTestCheckList(testCheckListBack);
        assertThat(securityTest.getTestCheckLists()).containsOnly(testCheckListBack);
        assertThat(testCheckListBack.getSecurityTest()).isEqualTo(securityTest);

        securityTest.removeTestCheckList(testCheckListBack);
        assertThat(securityTest.getTestCheckLists()).doesNotContain(testCheckListBack);
        assertThat(testCheckListBack.getSecurityTest()).isNull();

        securityTest.testCheckLists(new HashSet<>(Set.of(testCheckListBack)));
        assertThat(securityTest.getTestCheckLists()).containsOnly(testCheckListBack);
        assertThat(testCheckListBack.getSecurityTest()).isEqualTo(securityTest);

        securityTest.setTestCheckLists(new HashSet<>());
        assertThat(securityTest.getTestCheckLists()).doesNotContain(testCheckListBack);
        assertThat(testCheckListBack.getSecurityTest()).isNull();
    }

    @Test
    void applicationUserTest() throws Exception {
        SecurityTest securityTest = getSecurityTestRandomSampleGenerator();
        ApplicationUser applicationUserBack = getApplicationUserRandomSampleGenerator();

        securityTest.setApplicationUser(applicationUserBack);
        assertThat(securityTest.getApplicationUser()).isEqualTo(applicationUserBack);

        securityTest.applicationUser(null);
        assertThat(securityTest.getApplicationUser()).isNull();
    }
}
