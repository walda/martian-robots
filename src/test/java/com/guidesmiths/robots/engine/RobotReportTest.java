package com.guidesmiths.robots.engine;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThat;

public class RobotReportTest {

    @Test
    public void shouldPrintRobotReport() {
        assertThat(new RobotReport(Pair.of(1, 1), 'N', false).toString()).isEqualTo("1 1 N");
    }

    @Test
    public void shouldPrintRobotReportCrashed() {
        assertThat(new RobotReport(Pair.of(1, 1), 'N', true).toString()).isEqualTo("1 1 N LOST");
    }
}
