package com.guidesmiths.robots.tokenizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class ScannerWrapperTest {

    private ByteArrayInputStream byteArrayInputStream;
    private static final String INPUT_STRING = "1 1";
    private static final String EMPTY_STRING = "";

    @Test
    public void should_return_true_for_hasNext() {
        byteArrayInputStream = new ByteArrayInputStream(INPUT_STRING.getBytes());
        System.setIn(byteArrayInputStream);
        assertThat(new ScannerWrapper().hasNext())
        .isTrue();
    }

    @Test
    public void should_return_false_if_string_is_empty_for_hasNext() {
        byteArrayInputStream = new ByteArrayInputStream(EMPTY_STRING.getBytes());
        System.setIn(byteArrayInputStream);
        assertThat(new ScannerWrapper().hasNext())
                .isFalse();
    }

    @Test
    public void should_return_nextLine() {
        byteArrayInputStream = new ByteArrayInputStream(INPUT_STRING.getBytes());
        System.setIn(byteArrayInputStream);
        assertThat(new ScannerWrapper().nextLine())
                .isEqualTo("1 1");
    }

}
