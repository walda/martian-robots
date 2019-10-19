package com.guidesmiths.robots.engine;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.tuple.Pair;

@Value
@AllArgsConstructor
public class RobotReport {

    private static final String REPORT_PATTERN = "%s %s %s %s";

    private Pair<Integer, Integer> lastPosition;
    private char direction;
    private boolean crashed;

    @Override
    public String toString() {
        return String.format(REPORT_PATTERN, lastPosition.getLeft(), lastPosition.getRight(), direction, crashed ? "LOST" : "");
    }

}
