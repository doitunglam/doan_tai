package com.booking.tai.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A RoomHotel.
 */
@Entity
@Table(name = "room_hotel")
public class RoomHotel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "room_name")
    private String roomName;

    @Column(name = "description")
    private String description;

    @Column(name = "room_status")
    private Integer roomStatus;

    @ManyToOne
    @JsonIgnoreProperties(value = { "hotel" }, allowSetters = true)
    private TypeRoom typeRoom;

    @OneToMany(mappedBy = "roomHotel")
    @JsonIgnoreProperties(value = { "roomHotel" }, allowSetters = true)
    private Set<ImageRoom> imageRooms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public RoomHotel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public RoomHotel roomName(String roomName) {
        this.setRoomName(roomName);
        return this;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getDescription() {
        return this.description;
    }

    public RoomHotel description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRoomStatus() {
        return this.roomStatus;
    }

    public RoomHotel roomStatus(Integer roomStatus) {
        this.setRoomStatus(roomStatus);
        return this;
    }

    public void setRoomStatus(Integer roomStatus) {
        this.roomStatus = roomStatus;
    }

    public TypeRoom getTypeRoom() {
        return this.typeRoom;
    }

    public void setTypeRoom(TypeRoom typeRoom) {
        this.typeRoom = typeRoom;
    }

    public RoomHotel typeRoom(TypeRoom typeRoom) {
        this.setTypeRoom(typeRoom);
        return this;
    }

    public Set<ImageRoom> getImageRooms() {
        return this.imageRooms;
    }

    public void setImageRooms(Set<ImageRoom> imageRooms) {
        if (this.imageRooms != null) {
            this.imageRooms.forEach(i -> i.setRoomHotel(null));
        }
        if (imageRooms != null) {
            imageRooms.forEach(i -> i.setRoomHotel(this));
        }
        this.imageRooms = imageRooms;
    }

    public RoomHotel imageRooms(Set<ImageRoom> imageRooms) {
        this.setImageRooms(imageRooms);
        return this;
    }

    public RoomHotel addImageRoom(ImageRoom imageRoom) {
        this.imageRooms.add(imageRoom);
        imageRoom.setRoomHotel(this);
        return this;
    }

    public RoomHotel removeImageRoom(ImageRoom imageRoom) {
        this.imageRooms.remove(imageRoom);
        imageRoom.setRoomHotel(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomHotel)) {
            return false;
        }
        return id != null && id.equals(((RoomHotel) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomHotel{" +
            "id=" + getId() +
            ", roomName='" + getRoomName() + "'" +
            ", description='" + getDescription() + "'" +
            ", roomStatus=" + getRoomStatus() +
            "}";
    }
}
