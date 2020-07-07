package jp.co.greensys.takeout.domain;

import static org.assertj.core.api.Assertions.assertThat;

import jp.co.greensys.takeout.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class PayTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Pay.class);
        Pay pay1 = new Pay();
        pay1.setId(1L);
        Pay pay2 = new Pay();
        pay2.setId(pay1.getId());
        assertThat(pay1).isEqualTo(pay2);
        pay2.setId(2L);
        assertThat(pay1).isNotEqualTo(pay2);
        pay1.setId(null);
        assertThat(pay1).isNotEqualTo(pay2);
    }
}
