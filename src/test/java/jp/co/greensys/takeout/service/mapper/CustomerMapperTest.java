package jp.co.greensys.takeout.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CustomerMapperTest {
    private CustomerMapper customerMapper;

    @BeforeEach
    public void setUp() {
        customerMapper = new CustomerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(customerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(customerMapper.fromId(null)).isNull();
    }
}
