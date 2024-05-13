package com.secitriy.analyzer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TestCheckList.
 */
@Entity
@Table(name = "test_check_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCheckList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @JsonIgnoreProperties(value = { "checkLisItems", "testCheckList" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private CheckList checkList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "testCheckList")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "checklistitem", "testCheckList" }, allowSetters = true)
    private Set<TestCheckLisItem> testCheckLisItems = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "testCheckLists", "applicationUser" }, allowSetters = true)
    private SecurityTest securityTest;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCheckList id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CheckList getCheckList() {
        return this.checkList;
    }

    public void setCheckList(CheckList checkList) {
        this.checkList = checkList;
    }

    public TestCheckList checkList(CheckList checkList) {
        this.setCheckList(checkList);
        return this;
    }

    public Set<TestCheckLisItem> getTestCheckLisItems() {
        return this.testCheckLisItems;
    }

    public void setTestCheckLisItems(Set<TestCheckLisItem> testCheckLisItems) {
        if (this.testCheckLisItems != null) {
            this.testCheckLisItems.forEach(i -> i.setTestCheckList(null));
        }
        if (testCheckLisItems != null) {
            testCheckLisItems.forEach(i -> i.setTestCheckList(this));
        }
        this.testCheckLisItems = testCheckLisItems;
    }

    public TestCheckList testCheckLisItems(Set<TestCheckLisItem> testCheckLisItems) {
        this.setTestCheckLisItems(testCheckLisItems);
        return this;
    }

    public TestCheckList addTestCheckLisItem(TestCheckLisItem testCheckLisItem) {
        this.testCheckLisItems.add(testCheckLisItem);
        testCheckLisItem.setTestCheckList(this);
        return this;
    }

    public TestCheckList removeTestCheckLisItem(TestCheckLisItem testCheckLisItem) {
        this.testCheckLisItems.remove(testCheckLisItem);
        testCheckLisItem.setTestCheckList(null);
        return this;
    }

    public SecurityTest getSecurityTest() {
        return this.securityTest;
    }

    public void setSecurityTest(SecurityTest securityTest) {
        this.securityTest = securityTest;
    }

    public TestCheckList securityTest(SecurityTest securityTest) {
        this.setSecurityTest(securityTest);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCheckList)) {
            return false;
        }
        return getId() != null && getId().equals(((TestCheckList) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCheckList{" +
            "id=" + getId() +
            "}";
    }
}
