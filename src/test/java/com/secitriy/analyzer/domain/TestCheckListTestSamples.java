package com.secitriy.analyzer.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TestCheckListTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TestCheckList getTestCheckListSample1() {
        return new TestCheckList().id(1L);
    }

    public static TestCheckList getTestCheckListSample2() {
        return new TestCheckList().id(2L);
    }

    public static TestCheckList getTestCheckListRandomSampleGenerator() {
        return new TestCheckList().id(longCount.incrementAndGet());
    }
}
