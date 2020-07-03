package jp.co.greensys.takeout.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jp.co.greensys.takeout.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class OrderedDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrderedDTO.class);
        OrderedDTO orderedDTO1 = new OrderedDTO();
        orderedDTO1.setId("1L");
        OrderedDTO orderedDTO2 = new OrderedDTO();
        assertThat(orderedDTO1).isNotEqualTo(orderedDTO2);
        orderedDTO2.setId(orderedDTO1.getId());
        assertThat(orderedDTO1).isEqualTo(orderedDTO2);
        orderedDTO2.setId("2L");
        assertThat(orderedDTO1).isNotEqualTo(orderedDTO2);
        orderedDTO1.setId(null);
        assertThat(orderedDTO1).isNotEqualTo(orderedDTO2);
    }
}
