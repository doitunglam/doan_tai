package com.booking.tai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.booking.tai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PriceRoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PriceRoom.class);
        PriceRoom priceRoom1 = new PriceRoom();
        priceRoom1.setId(1L);
        PriceRoom priceRoom2 = new PriceRoom();
        priceRoom2.setId(priceRoom1.getId());
        assertThat(priceRoom1).isEqualTo(priceRoom2);
        priceRoom2.setId(2L);
        assertThat(priceRoom1).isNotEqualTo(priceRoom2);
        priceRoom1.setId(null);
        assertThat(priceRoom1).isNotEqualTo(priceRoom2);
    }
}
