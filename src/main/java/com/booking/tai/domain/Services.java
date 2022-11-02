package com.booking.tai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Services.
 */
@Entity
@Table(name = "services")
public class Services implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "service_name")
    private String serviceName;

    @DecimalMin(value = "0")
    @Column(name = "price")
    private Double price;

    @Column(name = "unit")
    private String unit;

    @Column(name = "quantity")
    private Float quantity;

    @Column(name = "address")
    private String address;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties(value = { "users", "services" }, allowSetters = true)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "services")
    @JsonIgnoreProperties(value = { "services" }, allowSetters = true)
    private Set<ImageServices> imageServices = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "services" }, allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Services id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getServiceName() {
        return this.serviceName;
    }

    public Services serviceName(String serviceName) {
        this.setServiceName(serviceName);
        return this;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Double getPrice() {
        return this.price;
    }

    public Services price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUnit() {
        return this.unit;
    }

    public Services unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Float getQuantity() {
        return this.quantity;
    }

    public Services quantity(Float quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getAddress() {
        return this.address;
    }

    public Services address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Restaurant getRestaurant() {
        return this.restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public Services restaurant(Restaurant restaurant) {
        this.setRestaurant(restaurant);
        return this;
    }

    public Set<ImageServices> getImageServices() {
        return this.imageServices;
    }

    public void setImageServices(Set<ImageServices> imageServices) {
        if (this.imageServices != null) {
            this.imageServices.forEach(i -> i.setServices(null));
        }
        if (imageServices != null) {
            imageServices.forEach(i -> i.setServices(this));
        }
        this.imageServices = imageServices;
    }

    public Services imageServices(Set<ImageServices> imageServices) {
        this.setImageServices(imageServices);
        return this;
    }

    public Services addImageServices(ImageServices imageServices) {
        this.imageServices.add(imageServices);
        imageServices.setServices(this);
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Services removeImageServices(ImageServices imageServices) {
        this.imageServices.remove(imageServices);
        imageServices.setServices(null);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Services category(Category category) {
        this.setCategory(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Services)) {
            return false;
        }
        return id != null && id.equals(((Services) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Services{" +
            "id=" + getId() +
            ", serviceName='" + getServiceName() + "'" +
            ", price=" + getPrice() +
            ", unit='" + getUnit() + "'" +
            ", quantity=" + getQuantity() +
            ", address='" + getAddress() + "'" +
            "}";
    }
}
