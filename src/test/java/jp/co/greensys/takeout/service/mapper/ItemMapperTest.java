package jp.co.greensys.takeout.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ItemMapperTest {
    private ItemMapper itemMapper;

    @BeforeEach
    public void setUp() {
        itemMapper = new ItemMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(itemMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(itemMapper.fromId(null)).isNull();
    }
}
