package com.booking.tai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.booking.tai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageRoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageRoom.class);
        ImageRoom imageRoom1 = new ImageRoom();
        imageRoom1.setId(1L);
        ImageRoom imageRoom2 = new ImageRoom();
        imageRoom2.setId(imageRoom1.getId());
        assertThat(imageRoom1).isEqualTo(imageRoom2);
        imageRoom2.setId(2L);
        assertThat(imageRoom1).isNotEqualTo(imageRoom2);
        imageRoom1.setId(null);
        assertThat(imageRoom1).isNotEqualTo(imageRoom2);
    }
}
