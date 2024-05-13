package com.secitriy.analyzer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CheckLisItem.
 */
@Entity
@Table(name = "check_lis_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CheckLisItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private Double value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "checkLisItems", "testCheckList" }, allowSetters = true)
    private CheckList checkList;

    @JsonIgnoreProperties(value = { "checklistitem", "testCheckList" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "checklistitem")
    private TestCheckLisItem testCheckLisItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CheckLisItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public CheckLisItem name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return this.value;
    }

    public CheckLisItem value(Double value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public CheckList getCheckList() {
        return this.checkList;
    }

    public void setCheckList(CheckList checkList) {
        this.checkList = checkList;
    }

    public CheckLisItem checkList(CheckList checkList) {
        this.setCheckList(checkList);
        return this;
    }

    public TestCheckLisItem getTestCheckLisItem() {
        return this.testCheckLisItem;
    }

    public void setTestCheckLisItem(TestCheckLisItem testCheckLisItem) {
        if (this.testCheckLisItem != null) {
            this.testCheckLisItem.setChecklistitem(null);
        }
        if (testCheckLisItem != null) {
            testCheckLisItem.setChecklistitem(this);
        }
        this.testCheckLisItem = testCheckLisItem;
    }

    public CheckLisItem testCheckLisItem(TestCheckLisItem testCheckLisItem) {
        this.setTestCheckLisItem(testCheckLisItem);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CheckLisItem)) {
            return false;
        }
        return getId() != null && getId().equals(((CheckLisItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CheckLisItem{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", value=" + getValue() +
            "}";
    }
}
