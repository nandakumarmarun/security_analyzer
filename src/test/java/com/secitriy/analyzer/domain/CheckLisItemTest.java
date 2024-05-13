package com.secitriy.analyzer.domain;

import static com.secitriy.analyzer.domain.CheckLisItemTestSamples.*;
import static com.secitriy.analyzer.domain.CheckListTestSamples.*;
import static com.secitriy.analyzer.domain.TestCheckLisItemTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.secitriy.analyzer.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CheckLisItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CheckLisItem.class);
        CheckLisItem checkLisItem1 = getCheckLisItemSample1();
        CheckLisItem checkLisItem2 = new CheckLisItem();
        assertThat(checkLisItem1).isNotEqualTo(checkLisItem2);

        checkLisItem2.setId(checkLisItem1.getId());
        assertThat(checkLisItem1).isEqualTo(checkLisItem2);

        checkLisItem2 = getCheckLisItemSample2();
        assertThat(checkLisItem1).isNotEqualTo(checkLisItem2);
    }

    @Test
    void checkListTest() throws Exception {
        CheckLisItem checkLisItem = getCheckLisItemRandomSampleGenerator();
        CheckList checkListBack = getCheckListRandomSampleGenerator();

        checkLisItem.setCheckList(checkListBack);
        assertThat(checkLisItem.getCheckList()).isEqualTo(checkListBack);

        checkLisItem.checkList(null);
        assertThat(checkLisItem.getCheckList()).isNull();
    }

    @Test
    void testCheckLisItemTest() throws Exception {
        CheckLisItem checkLisItem = getCheckLisItemRandomSampleGenerator();
        TestCheckLisItem testCheckLisItemBack = getTestCheckLisItemRandomSampleGenerator();

        checkLisItem.setTestCheckLisItem(testCheckLisItemBack);
        assertThat(checkLisItem.getTestCheckLisItem()).isEqualTo(testCheckLisItemBack);
        assertThat(testCheckLisItemBack.getChecklistitem()).isEqualTo(checkLisItem);

        checkLisItem.testCheckLisItem(null);
        assertThat(checkLisItem.getTestCheckLisItem()).isNull();
        assertThat(testCheckLisItemBack.getChecklistitem()).isNull();
    }
}
