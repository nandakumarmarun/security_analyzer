package com.secitriy.analyzer.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class SecurityTestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SecurityTest getSecurityTestSample1() {
        return new SecurityTest().id(1L).testStatus("testStatus1");
    }

    public static SecurityTest getSecurityTestSample2() {
        return new SecurityTest().id(2L).testStatus("testStatus2");
    }

    public static SecurityTest getSecurityTestRandomSampleGenerator() {
        return new SecurityTest().id(longCount.incrementAndGet()).testStatus(UUID.randomUUID().toString());
    }
}
