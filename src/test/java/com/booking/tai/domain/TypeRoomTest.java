package com.booking.tai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.booking.tai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TypeRoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TypeRoom.class);
        TypeRoom typeRoom1 = new TypeRoom();
        typeRoom1.setId(1L);
        TypeRoom typeRoom2 = new TypeRoom();
        typeRoom2.setId(typeRoom1.getId());
        assertThat(typeRoom1).isEqualTo(typeRoom2);
        typeRoom2.setId(2L);
        assertThat(typeRoom1).isNotEqualTo(typeRoom2);
        typeRoom1.setId(null);
        assertThat(typeRoom1).isNotEqualTo(typeRoom2);
    }
}
