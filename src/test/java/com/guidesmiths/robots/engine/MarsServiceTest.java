package com.guidesmiths.robots.engine;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
public class MarsServiceTest {

    @InjectMocks
    private MarsService marsService;

    @Test
    public void shouldInitializeMarsService() throws IllegalAccessException {

        marsService.init(1, 1, new HashMap<>());

        assertThat(FieldUtils.readDeclaredField(marsService, "upper_right_x", true)).isEqualTo(1);
        assertThat(FieldUtils.readDeclaredField(marsService, "upper_right_y", true)).isEqualTo(1);
        assertThat(FieldUtils.readDeclaredField(marsService, "exploredFields", true)).isEqualTo(new HashMap<>());

    }

    @Test
    public void shouldExplore_a_InsideMars() {
        marsService.init(0, 0, new HashMap<>());
        assertThat(marsService.explore(Pair.of(0, 0), 'W')).isEqualTo(MarsService.FIELD_STATUS.EXPLORED);
    }

    @Test
    public void shouldExplore_a_OutsideMars_From_North() {
        marsService.init(0, 0, new HashMap<>());
        assertThat(marsService.explore(Pair.of(0, 1), 'W')).isEqualTo(MarsService.FIELD_STATUS.CRASHED);
    }

    @Test
    public void shouldExplore_a_OutsideMars_From_South() {
        marsService.init(0, 0, new HashMap<>());
        assertThat(marsService.explore(Pair.of(0, -1), 'W')).isEqualTo(MarsService.FIELD_STATUS.CRASHED);
    }

    @Test
    public void shouldExplore_a_OutsideMars_From_East() {
        marsService.init(0, 0, new HashMap<>());
        assertThat(marsService.explore(Pair.of(1, 0), 'W')).isEqualTo(MarsService.FIELD_STATUS.CRASHED);
    }

    @Test
    public void shouldExplore_a_OutsideMars_From_West() {
        marsService.init(0, 0, new HashMap<>());
        assertThat(marsService.explore(Pair.of(-1, 0), 'W')).isEqualTo(MarsService.FIELD_STATUS.CRASHED);
    }

    @Test
    public void should_Check_Field_Before_Exploring() {
        marsService.init(0, 0, new HashMap<>());
        assertThat(marsService.checkField(Pair.of(0,0), 'W')).isEqualTo(MarsService.FIELD_STATUS.UNKNOWN);
    }

    @Test
    public void should_Check_Field_After_Exploring() {
        marsService.init(0, 0, new HashMap<>());
        marsService.explore(Pair.of(0,0), 'W');
        assertThat(marsService.checkField(Pair.of(0,0), 'W')).isEqualTo(MarsService.FIELD_STATUS.EXPLORED);
    }

    @Test
    public void should_Check_Field_After_Exploring_Different_Direction() {
        marsService.init(0, 0, new HashMap<>());
        marsService.explore(Pair.of(0,0), 'W');
        assertThat(marsService.checkField(Pair.of(0,0), 'E')).isEqualTo(MarsService.FIELD_STATUS.UNKNOWN);
    }

    @Test
    public void should_Check_Field_After_Exploring_Different_Field_East() {
        marsService.init(1, 1, new HashMap<>());
        marsService.explore(Pair.of(0,0), 'W');
        assertThat(marsService.checkField(Pair.of(1,0), 'E')).isEqualTo(MarsService.FIELD_STATUS.UNKNOWN);
    }

    @Test
    public void should_Check_Field_After_Exploring_Different_Field_North() {
        marsService.init(1, 1, new HashMap<>());
        marsService.explore(Pair.of(0,0), 'W');
        assertThat(marsService.checkField(Pair.of(0,1), 'E')).isEqualTo(MarsService.FIELD_STATUS.UNKNOWN);
    }
}
