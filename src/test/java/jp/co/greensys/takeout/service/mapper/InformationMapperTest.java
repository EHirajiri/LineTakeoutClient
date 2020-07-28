package jp.co.greensys.takeout.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InformationMapperTest {
    private InformationMapper informationMapper;

    @BeforeEach
    public void setUp() {
        informationMapper = new InformationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(informationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(informationMapper.fromId(null)).isNull();
    }
}
