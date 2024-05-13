package com.secitriy.analyzer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TestCheckLisItem.
 */
@Entity
@Table(name = "test_check_lis_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestCheckLisItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "marked")
    private Boolean marked;

    @JsonIgnoreProperties(value = { "checkList", "testCheckLisItem" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private CheckLisItem checklistitem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "checkList", "testCheckLisItems", "securityTest" }, allowSetters = true)
    private TestCheckList testCheckList;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestCheckLisItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getMarked() {
        return this.marked;
    }

    public TestCheckLisItem marked(Boolean marked) {
        this.setMarked(marked);
        return this;
    }

    public void setMarked(Boolean marked) {
        this.marked = marked;
    }

    public CheckLisItem getChecklistitem() {
        return this.checklistitem;
    }

    public void setChecklistitem(CheckLisItem checkLisItem) {
        this.checklistitem = checkLisItem;
    }

    public TestCheckLisItem checklistitem(CheckLisItem checkLisItem) {
        this.setChecklistitem(checkLisItem);
        return this;
    }

    public TestCheckList getTestCheckList() {
        return this.testCheckList;
    }

    public void setTestCheckList(TestCheckList testCheckList) {
        this.testCheckList = testCheckList;
    }

    public TestCheckLisItem testCheckList(TestCheckList testCheckList) {
        this.setTestCheckList(testCheckList);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestCheckLisItem)) {
            return false;
        }
        return getId() != null && getId().equals(((TestCheckLisItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestCheckLisItem{" +
            "id=" + getId() +
            ", marked='" + getMarked() + "'" +
            "}";
    }
}
