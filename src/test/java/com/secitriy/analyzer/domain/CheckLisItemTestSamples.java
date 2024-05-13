package com.secitriy.analyzer.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CheckLisItemTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CheckLisItem getCheckLisItemSample1() {
        return new CheckLisItem().id(1L).name("name1");
    }

    public static CheckLisItem getCheckLisItemSample2() {
        return new CheckLisItem().id(2L).name("name2");
    }

    public static CheckLisItem getCheckLisItemRandomSampleGenerator() {
        return new CheckLisItem().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
