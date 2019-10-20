package com.guidesmiths.robots.exception;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class InvalidRuleExceptionTest {

    @Test
    public void shouldCreateExceptionWithMessage() {
        assertThat(new InvalidRuleException('F', 'N'))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("No rule found for instruction F and direction N");
    }

}
