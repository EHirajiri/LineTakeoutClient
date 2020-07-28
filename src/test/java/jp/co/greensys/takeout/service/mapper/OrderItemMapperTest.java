package jp.co.greensys.takeout.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderItemMapperTest {
    private OrderItemMapper orderItemMapper;

    @BeforeEach
    public void setUp() {
        orderItemMapper = new OrderItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(orderItemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(orderItemMapper.fromId(null)).isNull();
    }
}
