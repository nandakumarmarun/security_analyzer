package com.secitriy.analyzer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A State.
 */
@Entity
@Table(name = "state")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class State implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "state")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "state", "city", "district", "userAttributes" }, allowSetters = true)
    private Set<Location> locations = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "state")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "locations", "state", "userAttributes" }, allowSetters = true)
    private Set<District> districts = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "states", "cities", "userAttributes" }, allowSetters = true)
    private Country country;

    @JsonIgnoreProperties(value = { "country", "state", "district", "city", "location", "applicationUser" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "state")
    private UserAttributes userAttributes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public State id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public State name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Location> getLocations() {
        return this.locations;
    }

    public void setLocations(Set<Location> locations) {
        if (this.locations != null) {
            this.locations.forEach(i -> i.setState(null));
        }
        if (locations != null) {
            locations.forEach(i -> i.setState(this));
        }
        this.locations = locations;
    }

    public State locations(Set<Location> locations) {
        this.setLocations(locations);
        return this;
    }

    public State addLocation(Location location) {
        this.locations.add(location);
        location.setState(this);
        return this;
    }

    public State removeLocation(Location location) {
        this.locations.remove(location);
        location.setState(null);
        return this;
    }

    public Set<District> getDistricts() {
        return this.districts;
    }

    public void setDistricts(Set<District> districts) {
        if (this.districts != null) {
            this.districts.forEach(i -> i.setState(null));
        }
        if (districts != null) {
            districts.forEach(i -> i.setState(this));
        }
        this.districts = districts;
    }

    public State districts(Set<District> districts) {
        this.setDistricts(districts);
        return this;
    }

    public State addDistrict(District district) {
        this.districts.add(district);
        district.setState(this);
        return this;
    }

    public State removeDistrict(District district) {
        this.districts.remove(district);
        district.setState(null);
        return this;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public State country(Country country) {
        this.setCountry(country);
        return this;
    }

    public UserAttributes getUserAttributes() {
        return this.userAttributes;
    }

    public void setUserAttributes(UserAttributes userAttributes) {
        if (this.userAttributes != null) {
            this.userAttributes.setState(null);
        }
        if (userAttributes != null) {
            userAttributes.setState(this);
        }
        this.userAttributes = userAttributes;
    }

    public State userAttributes(UserAttributes userAttributes) {
        this.setUserAttributes(userAttributes);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof State)) {
            return false;
        }
        return getId() != null && getId().equals(((State) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "State{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
