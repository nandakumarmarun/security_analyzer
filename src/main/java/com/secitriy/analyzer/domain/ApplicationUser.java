package com.secitriy.analyzer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApplicationUser.
 */
@Entity
@Table(name = "application_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ApplicationUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private User internalUser;

    @JsonIgnoreProperties(value = { "country", "state", "district", "city", "location", "applicationUser" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private UserAttributes userAttributes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "applicationUser")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "testCheckLists", "applicationUser" }, allowSetters = true)
    private Set<SecurityTest> securityTests = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationUser id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ApplicationUser name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public ApplicationUser internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    public UserAttributes getUserAttributes() {
        return this.userAttributes;
    }

    public void setUserAttributes(UserAttributes userAttributes) {
        this.userAttributes = userAttributes;
    }

    public ApplicationUser userAttributes(UserAttributes userAttributes) {
        this.setUserAttributes(userAttributes);
        return this;
    }

    public Set<SecurityTest> getSecurityTests() {
        return this.securityTests;
    }

    public void setSecurityTests(Set<SecurityTest> securityTests) {
        if (this.securityTests != null) {
            this.securityTests.forEach(i -> i.setApplicationUser(null));
        }
        if (securityTests != null) {
            securityTests.forEach(i -> i.setApplicationUser(this));
        }
        this.securityTests = securityTests;
    }

    public ApplicationUser securityTests(Set<SecurityTest> securityTests) {
        this.setSecurityTests(securityTests);
        return this;
    }

    public ApplicationUser addSecurityTest(SecurityTest securityTest) {
        this.securityTests.add(securityTest);
        securityTest.setApplicationUser(this);
        return this;
    }

    public ApplicationUser removeSecurityTest(SecurityTest securityTest) {
        this.securityTests.remove(securityTest);
        securityTest.setApplicationUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationUser)) {
            return false;
        }
        return getId() != null && getId().equals(((ApplicationUser) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationUser{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
