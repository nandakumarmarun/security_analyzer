package com.secitriy.analyzer.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TestCheckLisItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TestCheckLisItem getTestCheckLisItemSample1() {
        return new TestCheckLisItem().id(1L);
    }

    public static TestCheckLisItem getTestCheckLisItemSample2() {
        return new TestCheckLisItem().id(2L);
    }

    public static TestCheckLisItem getTestCheckLisItemRandomSampleGenerator() {
        return new TestCheckLisItem().id(longCount.incrementAndGet());
    }
}
