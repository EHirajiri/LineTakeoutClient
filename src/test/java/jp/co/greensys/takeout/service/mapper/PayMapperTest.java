package jp.co.greensys.takeout.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PayMapperTest {
    private PayMapper payMapper;

    @BeforeEach
    public void setUp() {
        payMapper = new PayMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(payMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(payMapper.fromId(null)).isNull();
    }
}
