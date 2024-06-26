package com.secitriy.analyzer.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAttributesAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserAttributesAllPropertiesEquals(UserAttributes expected, UserAttributes actual) {
        assertUserAttributesAutoGeneratedPropertiesEquals(expected, actual);
        assertUserAttributesAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserAttributesAllUpdatablePropertiesEquals(UserAttributes expected, UserAttributes actual) {
        assertUserAttributesUpdatableFieldsEquals(expected, actual);
        assertUserAttributesUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserAttributesAutoGeneratedPropertiesEquals(UserAttributes expected, UserAttributes actual) {
        assertThat(expected)
            .as("Verify UserAttributes auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserAttributesUpdatableFieldsEquals(UserAttributes expected, UserAttributes actual) {
        assertThat(expected)
            .as("Verify UserAttributes relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getPhone()).as("check phone").isEqualTo(actual.getPhone()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getAddress()).as("check address").isEqualTo(actual.getAddress()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserAttributesUpdatableRelationshipsEquals(UserAttributes expected, UserAttributes actual) {
        assertThat(expected)
            .as("Verify UserAttributes relationships")
            .satisfies(e -> assertThat(e.getCountry()).as("check country").isEqualTo(actual.getCountry()))
            .satisfies(e -> assertThat(e.getState()).as("check state").isEqualTo(actual.getState()))
            .satisfies(e -> assertThat(e.getDistrict()).as("check district").isEqualTo(actual.getDistrict()))
            .satisfies(e -> assertThat(e.getCity()).as("check city").isEqualTo(actual.getCity()))
            .satisfies(e -> assertThat(e.getLocation()).as("check location").isEqualTo(actual.getLocation()));
    }
}
