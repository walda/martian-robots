package com.guidesmiths.robots.rule;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ActionTest {

    @Test
    public void should_Return_Null_When_No_Enum_Is_Found() {
        assertThat(Action.fromString("Q")).isNull();
    }

}
