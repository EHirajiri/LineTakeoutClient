package jp.co.greensys.takeout.domain;

import static org.assertj.core.api.Assertions.assertThat;

import jp.co.greensys.takeout.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

public class InformationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Information.class);
        Information information1 = new Information();
        information1.setId(1L);
        Information information2 = new Information();
        information2.setId(information1.getId());
        assertThat(information1).isEqualTo(information2);
        information2.setId(2L);
        assertThat(information1).isNotEqualTo(information2);
        information1.setId(null);
        assertThat(information1).isNotEqualTo(information2);
    }
}
