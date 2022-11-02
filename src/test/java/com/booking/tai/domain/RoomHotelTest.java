package com.booking.tai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.booking.tai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoomHotelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RoomHotel.class);
        RoomHotel roomHotel1 = new RoomHotel();
        roomHotel1.setId(1L);
        RoomHotel roomHotel2 = new RoomHotel();
        roomHotel2.setId(roomHotel1.getId());
        assertThat(roomHotel1).isEqualTo(roomHotel2);
        roomHotel2.setId(2L);
        assertThat(roomHotel1).isNotEqualTo(roomHotel2);
        roomHotel1.setId(null);
        assertThat(roomHotel1).isNotEqualTo(roomHotel2);
    }
}
