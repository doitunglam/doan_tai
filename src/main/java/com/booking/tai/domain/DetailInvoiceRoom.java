package com.booking.tai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

/**
 * A DetailInvoiceRoom.
 */
@Entity
@Table(name = "detail_invoice_room")
public class DetailInvoiceRoom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "price")
    private Double price;

    @Column(name = "booking_date")
    private Timestamp bookingDate;

    @Column(name = "return_date")
    private Timestamp returnDate;

    @ManyToOne
    @JsonIgnoreProperties(value = { "roomHotel" }, allowSetters = true)
    private PriceRoom priceRoom;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Invoice invoice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DetailInvoiceRoom id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return this.price;
    }

    public DetailInvoiceRoom price(Double price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public PriceRoom getPriceRoom() {
        return this.priceRoom;
    }

    public Timestamp getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Timestamp bookingDate) {
        this.bookingDate = bookingDate;
    }

    public Timestamp getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Timestamp returnDate) {
        this.returnDate = returnDate;
    }

    public void setPriceRoom(PriceRoom priceRoom) {
        this.priceRoom = priceRoom;
    }

    public DetailInvoiceRoom priceRoom(PriceRoom priceRoom) {
        this.setPriceRoom(priceRoom);
        return this;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public DetailInvoiceRoom invoice(Invoice invoice) {
        this.setInvoice(invoice);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DetailInvoiceRoom)) {
            return false;
        }
        return id != null && id.equals(((DetailInvoiceRoom) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DetailInvoiceRoom{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            "}";
    }
}
