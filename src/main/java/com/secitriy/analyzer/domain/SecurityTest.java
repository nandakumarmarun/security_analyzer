package com.secitriy.analyzer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.secitriy.analyzer.domain.enumeration.SecurityLevel;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SecurityTest.
 */
@Entity
@Table(name = "security_test")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SecurityTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "test_status")
    private String testStatus;

    @Column(name = "test_score")
    private Double testScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "security_level")
    private SecurityLevel securityLevel;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "securityTest")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "checkList", "testCheckLisItems", "securityTest" }, allowSetters = true)
    private Set<TestCheckList> testCheckLists = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "internalUser", "userAttributes", "securityTests" }, allowSetters = true)
    private ApplicationUser applicationUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SecurityTest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTestStatus() {
        return this.testStatus;
    }

    public SecurityTest testStatus(String testStatus) {
        this.setTestStatus(testStatus);
        return this;
    }

    public void setTestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    public Double getTestScore() {
        return this.testScore;
    }

    public SecurityTest testScore(Double testScore) {
        this.setTestScore(testScore);
        return this;
    }

    public void setTestScore(Double testScore) {
        this.testScore = testScore;
    }

    public SecurityLevel getSecurityLevel() {
        return this.securityLevel;
    }

    public SecurityTest securityLevel(SecurityLevel securityLevel) {
        this.setSecurityLevel(securityLevel);
        return this;
    }

    public void setSecurityLevel(SecurityLevel securityLevel) {
        this.securityLevel = securityLevel;
    }

    public Set<TestCheckList> getTestCheckLists() {
        return this.testCheckLists;
    }

    public void setTestCheckLists(Set<TestCheckList> testCheckLists) {
        if (this.testCheckLists != null) {
            this.testCheckLists.forEach(i -> i.setSecurityTest(null));
        }
        if (testCheckLists != null) {
            testCheckLists.forEach(i -> i.setSecurityTest(this));
        }
        this.testCheckLists = testCheckLists;
    }

    public SecurityTest testCheckLists(Set<TestCheckList> testCheckLists) {
        this.setTestCheckLists(testCheckLists);
        return this;
    }

    public SecurityTest addTestCheckList(TestCheckList testCheckList) {
        this.testCheckLists.add(testCheckList);
        testCheckList.setSecurityTest(this);
        return this;
    }

    public SecurityTest removeTestCheckList(TestCheckList testCheckList) {
        this.testCheckLists.remove(testCheckList);
        testCheckList.setSecurityTest(null);
        return this;
    }

    public ApplicationUser getApplicationUser() {
        return this.applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public SecurityTest applicationUser(ApplicationUser applicationUser) {
        this.setApplicationUser(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SecurityTest)) {
            return false;
        }
        return getId() != null && getId().equals(((SecurityTest) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SecurityTest{" +
            "id=" + getId() +
            ", testStatus='" + getTestStatus() + "'" +
            ", testScore=" + getTestScore() +
            ", securityLevel='" + getSecurityLevel() + "'" +
            "}";
    }
}
