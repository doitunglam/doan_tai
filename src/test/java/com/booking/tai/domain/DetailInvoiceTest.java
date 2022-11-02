package com.booking.tai.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.booking.tai.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DetailInvoiceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DetailInvoice.class);
        DetailInvoice detailInvoice1 = new DetailInvoice();
        detailInvoice1.setId(1L);
        DetailInvoice detailInvoice2 = new DetailInvoice();
        detailInvoice2.setId(detailInvoice1.getId());
        assertThat(detailInvoice1).isEqualTo(detailInvoice2);
        detailInvoice2.setId(2L);
        assertThat(detailInvoice1).isNotEqualTo(detailInvoice2);
        detailInvoice1.setId(null);
        assertThat(detailInvoice1).isNotEqualTo(detailInvoice2);
    }
}
