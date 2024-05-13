package com.secitriy.analyzer.domain;

import static com.secitriy.analyzer.domain.CheckLisItemTestSamples.*;
import static com.secitriy.analyzer.domain.TestCheckLisItemTestSamples.*;
import static com.secitriy.analyzer.domain.TestCheckListTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.secitriy.analyzer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestCheckLisItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCheckLisItem.class);
        TestCheckLisItem testCheckLisItem1 = getTestCheckLisItemSample1();
        TestCheckLisItem testCheckLisItem2 = new TestCheckLisItem();
        assertThat(testCheckLisItem1).isNotEqualTo(testCheckLisItem2);

        testCheckLisItem2.setId(testCheckLisItem1.getId());
        assertThat(testCheckLisItem1).isEqualTo(testCheckLisItem2);

        testCheckLisItem2 = getTestCheckLisItemSample2();
        assertThat(testCheckLisItem1).isNotEqualTo(testCheckLisItem2);
    }

    @Test
    void checklistitemTest() throws Exception {
        TestCheckLisItem testCheckLisItem = getTestCheckLisItemRandomSampleGenerator();
        CheckLisItem checkLisItemBack = getCheckLisItemRandomSampleGenerator();

        testCheckLisItem.setChecklistitem(checkLisItemBack);
        assertThat(testCheckLisItem.getChecklistitem()).isEqualTo(checkLisItemBack);

        testCheckLisItem.checklistitem(null);
        assertThat(testCheckLisItem.getChecklistitem()).isNull();
    }

    @Test
    void testCheckListTest() throws Exception {
        TestCheckLisItem testCheckLisItem = getTestCheckLisItemRandomSampleGenerator();
        TestCheckList testCheckListBack = getTestCheckListRandomSampleGenerator();

        testCheckLisItem.setTestCheckList(testCheckListBack);
        assertThat(testCheckLisItem.getTestCheckList()).isEqualTo(testCheckListBack);

        testCheckLisItem.testCheckList(null);
        assertThat(testCheckLisItem.getTestCheckList()).isNull();
    }
}
