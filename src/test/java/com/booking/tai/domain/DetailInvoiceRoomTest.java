package com.booking.tai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.booking.tai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetailInvoiceRoomTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailInvoiceRoom.class);
        DetailInvoiceRoom detailInvoiceRoom1 = new DetailInvoiceRoom();
        detailInvoiceRoom1.setId(1L);
        DetailInvoiceRoom detailInvoiceRoom2 = new DetailInvoiceRoom();
        detailInvoiceRoom2.setId(detailInvoiceRoom1.getId());
        assertThat(detailInvoiceRoom1).isEqualTo(detailInvoiceRoom2);
        detailInvoiceRoom2.setId(2L);
        assertThat(detailInvoiceRoom1).isNotEqualTo(detailInvoiceRoom2);
        detailInvoiceRoom1.setId(null);
        assertThat(detailInvoiceRoom1).isNotEqualTo(detailInvoiceRoom2);
    }
}
