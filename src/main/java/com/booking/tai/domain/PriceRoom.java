package com.booking.tai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A PriceRoom.
 */
@Entity
@Table(name = "price_room")
public class PriceRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "price")
    private Double price;

    @Column(name = "unit")
    private String unit;

    @ManyToOne
    @JsonIgnoreProperties(value = { "typeRoom", "imageRooms" }, allowSetters = true)
    private RoomHotel roomHotel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PriceRoom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return this.price;
    }

    public PriceRoom price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUnit() {
        return this.unit;
    }

    public PriceRoom unit(String unit) {
        this.setUnit(unit);
        return this;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public RoomHotel getRoomHotel() {
        return this.roomHotel;
    }

    public void setRoomHotel(RoomHotel roomHotel) {
        this.roomHotel = roomHotel;
    }

    public PriceRoom roomHotel(RoomHotel roomHotel) {
        this.setRoomHotel(roomHotel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceRoom)) {
            return false;
        }
        return id != null && id.equals(((PriceRoom) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceRoom{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", unit='" + getUnit() + "'" +
            "}";
    }
}
