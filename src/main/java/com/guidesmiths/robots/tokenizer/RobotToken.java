package com.guidesmiths.robots.tokenizer;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.tuple.Pair;

@Value
@AllArgsConstructor
public class RobotToken {

    private Pair<Integer, Integer> position;
    private char direction;

}
