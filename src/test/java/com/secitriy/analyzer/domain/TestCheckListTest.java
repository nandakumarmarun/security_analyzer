package com.secitriy.analyzer.domain;

import static com.secitriy.analyzer.domain.CheckListTestSamples.*;
import static com.secitriy.analyzer.domain.SecurityTestTestSamples.*;
import static com.secitriy.analyzer.domain.TestCheckLisItemTestSamples.*;
import static com.secitriy.analyzer.domain.TestCheckListTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.secitriy.analyzer.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TestCheckListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCheckList.class);
        TestCheckList testCheckList1 = getTestCheckListSample1();
        TestCheckList testCheckList2 = new TestCheckList();
        assertThat(testCheckList1).isNotEqualTo(testCheckList2);

        testCheckList2.setId(testCheckList1.getId());
        assertThat(testCheckList1).isEqualTo(testCheckList2);

        testCheckList2 = getTestCheckListSample2();
        assertThat(testCheckList1).isNotEqualTo(testCheckList2);
    }

    @Test
    void checkListTest() throws Exception {
        TestCheckList testCheckList = getTestCheckListRandomSampleGenerator();
        CheckList checkListBack = getCheckListRandomSampleGenerator();

        testCheckList.setCheckList(checkListBack);
        assertThat(testCheckList.getCheckList()).isEqualTo(checkListBack);

        testCheckList.checkList(null);
        assertThat(testCheckList.getCheckList()).isNull();
    }

    @Test
    void testCheckLisItemTest() throws Exception {
        TestCheckList testCheckList = getTestCheckListRandomSampleGenerator();
        TestCheckLisItem testCheckLisItemBack = getTestCheckLisItemRandomSampleGenerator();

        testCheckList.addTestCheckLisItem(testCheckLisItemBack);
        assertThat(testCheckList.getTestCheckLisItems()).containsOnly(testCheckLisItemBack);
        assertThat(testCheckLisItemBack.getTestCheckList()).isEqualTo(testCheckList);

        testCheckList.removeTestCheckLisItem(testCheckLisItemBack);
        assertThat(testCheckList.getTestCheckLisItems()).doesNotContain(testCheckLisItemBack);
        assertThat(testCheckLisItemBack.getTestCheckList()).isNull();

        testCheckList.testCheckLisItems(new HashSet<>(Set.of(testCheckLisItemBack)));
        assertThat(testCheckList.getTestCheckLisItems()).containsOnly(testCheckLisItemBack);
        assertThat(testCheckLisItemBack.getTestCheckList()).isEqualTo(testCheckList);

        testCheckList.setTestCheckLisItems(new HashSet<>());
        assertThat(testCheckList.getTestCheckLisItems()).doesNotContain(testCheckLisItemBack);
        assertThat(testCheckLisItemBack.getTestCheckList()).isNull();
    }

    @Test
    void securityTestTest() throws Exception {
        TestCheckList testCheckList = getTestCheckListRandomSampleGenerator();
        SecurityTest securityTestBack = getSecurityTestRandomSampleGenerator();

        testCheckList.setSecurityTest(securityTestBack);
        assertThat(testCheckList.getSecurityTest()).isEqualTo(securityTestBack);

        testCheckList.securityTest(null);
        assertThat(testCheckList.getSecurityTest()).isNull();
    }
}
