package com.guidesmiths.robots.tokenizer;

import com.guidesmiths.robots.exception.UnparseableTokenException;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class TokenizerService {

    private final static Pattern INIT_TOKEN_PATTERN = Pattern.compile("^([0-9]|[1-4][0-9]|50)\\s([0-9]|[1-4][0-9]|50)$");
    private final static Pattern ROBOT_TOKEN_PATTERN = Pattern.compile("^([0-9]|[1-4][0-9]|50)\\s([0-9]|[1-4][0-9]|50+)\\s([N,S,E,W]{1}+)$");
    private final static Pattern ROBOT_INSTRUCTION_PATTERN = Pattern.compile("^[R,L,F]{1,100}+$");

    public InitToken parseInitToken(String text) throws UnparseableTokenException {
        isValidPattern(INIT_TOKEN_PATTERN, text.trim());

        var matcher = INIT_TOKEN_PATTERN.matcher(text.trim());
        matcher.find();

        return new InitToken(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
    }

    public RobotToken parseRobotToken(String text) throws UnparseableTokenException {
        isValidPattern(ROBOT_TOKEN_PATTERN, text.trim());

        var matcher = ROBOT_TOKEN_PATTERN.matcher(text.trim());
        matcher.find();

        return new RobotToken(Pair.of(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2))), matcher.group(3).charAt(0));
    }

    public RobotInstructionToken parseRobotInstructionToken(String text) {
        isValidPattern(ROBOT_INSTRUCTION_PATTERN, text);

        var matcher = ROBOT_INSTRUCTION_PATTERN.matcher(text);
        matcher.find();

        return new RobotInstructionToken(matcher.group());
    }

    private static void isValidPattern(Pattern pattern, String text) {
        if(!pattern.matcher(text).matches()) {
            throw new UnparseableTokenException(text);
        }
    }

}
