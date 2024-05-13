package com.secitriy.analyzer.domain;

import static com.secitriy.analyzer.domain.CheckLisItemTestSamples.*;
import static com.secitriy.analyzer.domain.CheckListTestSamples.*;
import static com.secitriy.analyzer.domain.TestCheckListTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.secitriy.analyzer.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CheckListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CheckList.class);
        CheckList checkList1 = getCheckListSample1();
        CheckList checkList2 = new CheckList();
        assertThat(checkList1).isNotEqualTo(checkList2);

        checkList2.setId(checkList1.getId());
        assertThat(checkList1).isEqualTo(checkList2);

        checkList2 = getCheckListSample2();
        assertThat(checkList1).isNotEqualTo(checkList2);
    }

    @Test
    void checkLisItemTest() throws Exception {
        CheckList checkList = getCheckListRandomSampleGenerator();
        CheckLisItem checkLisItemBack = getCheckLisItemRandomSampleGenerator();

        checkList.addCheckLisItem(checkLisItemBack);
        assertThat(checkList.getCheckLisItems()).containsOnly(checkLisItemBack);
        assertThat(checkLisItemBack.getCheckList()).isEqualTo(checkList);

        checkList.removeCheckLisItem(checkLisItemBack);
        assertThat(checkList.getCheckLisItems()).doesNotContain(checkLisItemBack);
        assertThat(checkLisItemBack.getCheckList()).isNull();

        checkList.checkLisItems(new HashSet<>(Set.of(checkLisItemBack)));
        assertThat(checkList.getCheckLisItems()).containsOnly(checkLisItemBack);
        assertThat(checkLisItemBack.getCheckList()).isEqualTo(checkList);

        checkList.setCheckLisItems(new HashSet<>());
        assertThat(checkList.getCheckLisItems()).doesNotContain(checkLisItemBack);
        assertThat(checkLisItemBack.getCheckList()).isNull();
    }

    @Test
    void testCheckListTest() throws Exception {
        CheckList checkList = getCheckListRandomSampleGenerator();
        TestCheckList testCheckListBack = getTestCheckListRandomSampleGenerator();

        checkList.setTestCheckList(testCheckListBack);
        assertThat(checkList.getTestCheckList()).isEqualTo(testCheckListBack);
        assertThat(testCheckListBack.getCheckList()).isEqualTo(checkList);

        checkList.testCheckList(null);
        assertThat(checkList.getTestCheckList()).isNull();
        assertThat(testCheckListBack.getCheckList()).isNull();
    }
}
