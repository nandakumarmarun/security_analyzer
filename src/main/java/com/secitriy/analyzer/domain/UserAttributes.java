package com.secitriy.analyzer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserAttributes.
 */
@Entity
@Table(name = "user_attributes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserAttributes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "address")
    private String address;

    @JsonIgnoreProperties(value = { "states", "cities", "userAttributes" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Country country;

    @JsonIgnoreProperties(value = { "locations", "districts", "country", "userAttributes" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private State state;

    @JsonIgnoreProperties(value = { "locations", "state", "userAttributes" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private District district;

    @JsonIgnoreProperties(value = { "locations", "country", "userAttributes" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private City city;

    @JsonIgnoreProperties(value = { "state", "city", "district", "userAttributes" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Location location;

    @JsonIgnoreProperties(value = { "internalUser", "userAttributes", "securityTests" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userAttributes")
    private ApplicationUser applicationUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserAttributes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public UserAttributes name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public UserAttributes phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public UserAttributes email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public UserAttributes address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Country getCountry() {
        return this.country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public UserAttributes country(Country country) {
        this.setCountry(country);
        return this;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public UserAttributes state(State state) {
        this.setState(state);
        return this;
    }

    public District getDistrict() {
        return this.district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public UserAttributes district(District district) {
        this.setDistrict(district);
        return this;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public UserAttributes city(City city) {
        this.setCity(city);
        return this;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UserAttributes location(Location location) {
        this.setLocation(location);
        return this;
    }

    public ApplicationUser getApplicationUser() {
        return this.applicationUser;
    }

    public void setApplicationUser(ApplicationUser applicationUser) {
        if (this.applicationUser != null) {
            this.applicationUser.setUserAttributes(null);
        }
        if (applicationUser != null) {
            applicationUser.setUserAttributes(this);
        }
        this.applicationUser = applicationUser;
    }

    public UserAttributes applicationUser(ApplicationUser applicationUser) {
        this.setApplicationUser(applicationUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAttributes)) {
            return false;
        }
        return getId() != null && getId().equals(((UserAttributes) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAttributes{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
