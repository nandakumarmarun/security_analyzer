package com.secitriy.analyzer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CheckList.
 */
@Entity
@Table(name = "check_list")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CheckList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "checkList")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "checkList", "testCheckLisItem" }, allowSetters = true)
    private Set<CheckLisItem> checkLisItems = new HashSet<>();

    @JsonIgnoreProperties(value = { "checkList", "testCheckLisItems", "securityTest" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "checkList")
    private TestCheckList testCheckList;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CheckList id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public CheckList name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<CheckLisItem> getCheckLisItems() {
        return this.checkLisItems;
    }

    public void setCheckLisItems(Set<CheckLisItem> checkLisItems) {
        if (this.checkLisItems != null) {
            this.checkLisItems.forEach(i -> i.setCheckList(null));
        }
        if (checkLisItems != null) {
            checkLisItems.forEach(i -> i.setCheckList(this));
        }
        this.checkLisItems = checkLisItems;
    }

    public CheckList checkLisItems(Set<CheckLisItem> checkLisItems) {
        this.setCheckLisItems(checkLisItems);
        return this;
    }

    public CheckList addCheckLisItem(CheckLisItem checkLisItem) {
        this.checkLisItems.add(checkLisItem);
        checkLisItem.setCheckList(this);
        return this;
    }

    public CheckList removeCheckLisItem(CheckLisItem checkLisItem) {
        this.checkLisItems.remove(checkLisItem);
        checkLisItem.setCheckList(null);
        return this;
    }

    public TestCheckList getTestCheckList() {
        return this.testCheckList;
    }

    public void setTestCheckList(TestCheckList testCheckList) {
        if (this.testCheckList != null) {
            this.testCheckList.setCheckList(null);
        }
        if (testCheckList != null) {
            testCheckList.setCheckList(this);
        }
        this.testCheckList = testCheckList;
    }

    public CheckList testCheckList(TestCheckList testCheckList) {
        this.setTestCheckList(testCheckList);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CheckList)) {
            return false;
        }
        return getId() != null && getId().equals(((CheckList) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CheckList{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
