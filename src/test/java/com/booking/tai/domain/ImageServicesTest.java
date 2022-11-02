package com.booking.tai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.booking.tai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ImageServicesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ImageServices.class);
        ImageServices imageServices1 = new ImageServices();
        imageServices1.setId(1L);
        ImageServices imageServices2 = new ImageServices();
        imageServices2.setId(imageServices1.getId());
        assertThat(imageServices1).isEqualTo(imageServices2);
        imageServices2.setId(2L);
        assertThat(imageServices1).isNotEqualTo(imageServices2);
        imageServices1.setId(null);
        assertThat(imageServices1).isNotEqualTo(imageServices2);
    }
}
