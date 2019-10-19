package com.guidesmiths.robots.engine;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
class Mars {

    private static final String EXPLORED_FIELD_KEY_PATTERN = "%s_%s_%s";

    public enum FIELD_STATUS { EXPLORED, CRASHED, UNKNOWN };

    private int upper_right_x;
    private int upper_right_y;
    private Map<String, FIELD_STATUS> exploredFields;


    FIELD_STATUS explore(Pair<Integer, Integer> coordinates, char direction) {

        String key = String.format(EXPLORED_FIELD_KEY_PATTERN, coordinates.getLeft(), coordinates.getRight(), direction);

        if(exploredFields.containsKey(key)) {
            return exploredFields.get(key);
        } else if(isInside(coordinates)) {
            exploredFields.put(key, FIELD_STATUS.EXPLORED);
            return FIELD_STATUS.EXPLORED;
        } else {
            exploredFields.put(key, FIELD_STATUS.CRASHED);
            return FIELD_STATUS.CRASHED;
        }

    }

    FIELD_STATUS checkField(Pair<Integer, Integer> coordinates, char direction) {
        String key = String.format(EXPLORED_FIELD_KEY_PATTERN, coordinates.getLeft(), coordinates.getRight(), direction);
        return Optional.ofNullable(exploredFields.get(key)).orElse(FIELD_STATUS.UNKNOWN);
    }

    private boolean isInside(Pair<Integer, Integer> coordinates) {
        return coordinates.getLeft() <= upper_right_x && coordinates.getRight() <= upper_right_y
                && coordinates.getLeft() >= 0 && coordinates.getRight() >= 0;
    }
}
