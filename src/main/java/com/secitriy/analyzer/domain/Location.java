package com.secitriy.analyzer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "locations", "districts", "country", "userAttributes" }, allowSetters = true)
    private State state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "locations", "country", "userAttributes" }, allowSetters = true)
    private City city;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "locations", "state", "userAttributes" }, allowSetters = true)
    private District district;

    @JsonIgnoreProperties(value = { "country", "state", "district", "city", "location", "applicationUser" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "location")
    private UserAttributes userAttributes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Location id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Location name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public Location state(State state) {
        this.setState(state);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Location city(City city) {
        this.setCity(city);
        return this;
    }

    public District getDistrict() {
        return this.district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public Location district(District district) {
        this.setDistrict(district);
        return this;
    }

    public UserAttributes getUserAttributes() {
        return this.userAttributes;
    }

    public void setUserAttributes(UserAttributes userAttributes) {
        if (this.userAttributes != null) {
            this.userAttributes.setLocation(null);
        }
        if (userAttributes != null) {
            userAttributes.setLocation(this);
        }
        this.userAttributes = userAttributes;
    }

    public Location userAttributes(UserAttributes userAttributes) {
        this.setUserAttributes(userAttributes);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return getId() != null && getId().equals(((Location) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
