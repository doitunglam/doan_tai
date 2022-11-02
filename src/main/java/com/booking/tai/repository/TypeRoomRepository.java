package com.booking.tai.repository;

import com.booking.tai.domain.TypeRoom;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data SQL repository for the TypeRoom entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TypeRoomRepository extends JpaRepository<TypeRoom, Long> {

    @Query(value = "select t.* from type_room t where ", nativeQuery = true)
    List<TypeRoom> findByUser(Long idUser);
}
