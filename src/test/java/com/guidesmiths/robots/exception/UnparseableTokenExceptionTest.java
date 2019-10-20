package com.guidesmiths.robots.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UnparseableTokenExceptionTest {

    @Test
    public void shouldCreateExceptionWithMessage() {
        assertThat(new UnparseableTokenException("line"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Input 'line' cannot be parsed");
    }
}
