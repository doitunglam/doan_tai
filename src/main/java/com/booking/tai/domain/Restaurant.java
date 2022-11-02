package com.booking.tai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Restaurant.
 */
@Entity
@Table(name = "restaurant")
public class Restaurant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "res_name")
    private String resName;

    @Column(name = "address")
    private String address;

    @Size(max = 12)
    @Column(name = "phone", length = 12, unique = true)
    private String phone;

    @Column(name = "token")
    private String token;

    @Column(name = "linkweb")
    private String linkweb;

    @Column(name = "active")
    private Integer active;

    @ManyToMany
    @JoinTable(
        name = "rel_restaurant__user",
        joinColumns = @JoinColumn(name = "restaurant_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "restaurant")
    @JsonIgnoreProperties(value = { "restaurant", "imageServices", "category" }, allowSetters = true)
    private Set<Services> services = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Restaurant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResName() {
        return this.resName;
    }

    public Restaurant resName(String resName) {
        this.setResName(resName);
        return this;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getAddress() {
        return this.address;
    }

    public Restaurant address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return this.phone;
    }

    public Restaurant phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return this.token;
    }

    public Restaurant token(String token) {
        this.setToken(token);
        return this;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLinkweb() {
        return this.linkweb;
    }

    public Restaurant linkweb(String linkweb) {
        this.setLinkweb(linkweb);
        return this;
    }

    public void setLinkweb(String linkweb) {
        this.linkweb = linkweb;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Restaurant users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Restaurant addUser(User user) {
        this.users.add(user);
        return this;
    }

    public Restaurant removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Set<Services> getServices() {
        return this.services;
    }

    public void setServices(Set<Services> services) {
        if (this.services != null) {
            this.services.forEach(i -> i.setRestaurant(null));
        }
        if (services != null) {
            services.forEach(i -> i.setRestaurant(this));
        }
        this.services = services;
    }

    public Restaurant services(Set<Services> services) {
        this.setServices(services);
        return this;
    }

    public Restaurant addServices(Services services) {
        this.services.add(services);
        services.setRestaurant(this);
        return this;
    }

    public Restaurant removeServices(Services services) {
        this.services.remove(services);
        services.setRestaurant(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Restaurant)) {
            return false;
        }
        return id != null && id.equals(((Restaurant) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Restaurant{" +
            "id=" + getId() +
            ", resName='" + getResName() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", token='" + getToken() + "'" +
            ", linkweb='" + getLinkweb() + "'" +
            "}";
    }
}
