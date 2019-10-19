package com.guidesmiths.robots.engine;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

@Data
@Builder
class Robot {

    private Pair<Integer, Integer> position;
    private char direction;
    private String instructions;
    private int currentInstructionIndex;

    boolean hasNextInstruction() {
        return currentInstructionIndex < instructions.length();
    }

    char nextInstruction() {
        return  instructions.charAt(currentInstructionIndex++);
    }

}
