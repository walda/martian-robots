package com.guidesmiths.robots.tokenizer;

import com.guidesmiths.robots.exception.UnparseableTokenException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class TokenizerServiceTest {

    private final TokenizerService tokenizerService = new TokenizerService();

    @Test
    public void when_parseInitToken_a_token_is_returned() {
        assertThat(tokenizerService.parseInitToken("5 5"))
                .isNotNull()
                .isEqualTo(new InitToken(5, 5));
    }

    @Test
    public void when_parseInitToken_and_is_not_valid_then_exception_is_raised() {

        Throwable t = catchThrowable(() -> tokenizerService.parseInitToken("5 5g"));
        assertThat(t)
                .isNotNull()
                .isInstanceOf(UnparseableTokenException.class)
                .hasMessage("Input '5 5g' cannot be parsed");
    }

    @Test
    public void when_parseInitToken_and_x_is_higher_than_50_then_exception_is_raised() {

        Throwable t = catchThrowable(() -> tokenizerService.parseInitToken("51 50"));
        assertThat(t)
                .isNotNull()
                .isInstanceOf(UnparseableTokenException.class)
                .hasMessage("Input '51 50' cannot be parsed");
    }

    @Test
    public void when_parseInitToken_and_y_is_higher_than_50_then_exception_is_raised() {

        Throwable t = catchThrowable(() -> tokenizerService.parseInitToken("50 51"));
        assertThat(t)
                .isNotNull()
                .isInstanceOf(UnparseableTokenException.class)
                .hasMessage("Input '50 51' cannot be parsed");
    }

    @Test
    public void when_parseRobotToken_then_Token_is_returned() {
        assertThat(tokenizerService.parseRobotToken("1 1 N"))
            .isNotNull()
            .isEqualTo(new RobotToken(Pair.of(1, 1), 'N'));
    }

    @Test
    public void when_parseRobotToken_and_is_not_valid_then_exception_is_raised() {

        Throwable t = catchThrowable(() -> tokenizerService.parseRobotToken("1a 1 N"));

        assertThat(t)
                .isNotNull()
                .isInstanceOf(UnparseableTokenException.class)
                .hasMessage("Input '1a 1 N' cannot be parsed");

    }

    @Test
    public void when_parseRobotToken_and_coordinate_x_is_higher_than_50_then_exception_is_raised() {

        Throwable t = catchThrowable(() -> tokenizerService.parseRobotToken("51 50 N"));

        assertThat(t)
                .isNotNull()
                .isInstanceOf(UnparseableTokenException.class)
                .hasMessage("Input '51 50 N' cannot be parsed");

    }

    @Test
    public void when_parseRobotToken_and_coordinate_y_is_higher_than_50_then_exception_is_raised() {

        Throwable t = catchThrowable(() -> tokenizerService.parseRobotToken("50 51 N"));

        assertThat(t)
                .isNotNull()
                .isInstanceOf(UnparseableTokenException.class)
                .hasMessage("Input '50 51 N' cannot be parsed");

    }

    @Test
    public void when_parseRobotInstructionToken_then_a_token_is_returned() {
        assertThat(tokenizerService.parseRobotInstructionToken("LRF"))
            .isNotNull()
            .isEqualTo(new RobotInstructionToken("LRF"));
    }

    @Test
    public void when_parseRobotInstructionToken_with_length_100_then_a_token_is_returned() {
        assertThat(tokenizerService.parseRobotInstructionToken(createToken(100)))
                .isNotNull()
                .isEqualTo(new RobotInstructionToken(createToken(100)));
    }

    @Test
    public void when_parseRobotInstructionToken_and_token_is_invalid_then_exception_is_raised() {

        Throwable t = catchThrowable(() -> tokenizerService.parseRobotInstructionToken("LRFA"));

            assertThat(t)
                .isNotNull()
                .isInstanceOf(UnparseableTokenException.class)
                .hasMessage("Input 'LRFA' cannot be parsed");
    }

    @Test
    public void when_parseRobotInstructionToken_and_token_exceeds_100_then_exception_is_raised() {

        final String MESSAGE_PATTERN = "Input '%s' cannot be parsed";

        Throwable t = catchThrowable(() -> tokenizerService.parseRobotInstructionToken(createToken(101)));

        assertThat(t)
                .isNotNull()
                .isInstanceOf(UnparseableTokenException.class)
                .hasMessage(String.format(MESSAGE_PATTERN, createToken(101)));
    }

    private static String createToken(int size) {

        StringBuffer sb = new StringBuffer(size);
        for(int i=0; i < size; i++) {
            sb.append("L");
        }

        return sb.toString();
    }

}
