package jp.co.greensys.takeout.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jp.co.greensys.takeout.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class PayDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PayDTO.class);
        PayDTO payDTO1 = new PayDTO();
        payDTO1.setId(1L);
        PayDTO payDTO2 = new PayDTO();
        assertThat(payDTO1).isNotEqualTo(payDTO2);
        payDTO2.setId(payDTO1.getId());
        assertThat(payDTO1).isEqualTo(payDTO2);
        payDTO2.setId(2L);
        assertThat(payDTO1).isNotEqualTo(payDTO2);
        payDTO1.setId(null);
        assertThat(payDTO1).isNotEqualTo(payDTO2);
    }
}
