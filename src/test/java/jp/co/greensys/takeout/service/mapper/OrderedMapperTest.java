package jp.co.greensys.takeout.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OrderedMapperTest {
    private OrderedMapper orderedMapper;

    @BeforeEach
    public void setUp() {
        orderedMapper = new OrderedMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(orderedMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(orderedMapper.fromId(null)).isNull();
    }
}
