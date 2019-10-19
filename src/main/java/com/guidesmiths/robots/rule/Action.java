package com.guidesmiths.robots.rule;

import lombok.Getter;

@Getter
public enum Action {

    DIRECTION_N("N"),
    DIRECTION_E("E"),
    DIRECTION_S("S"),
    DIRECTION_W("W"),
    INCREASE_X("INCREASE_X"),
    INCREASE_Y("INCREASE_Y"),
    DECREASE_X("DECREASE_X"),
    DECREASE_Y("DECREASE_Y");

    private String value;

    Action(String value) {
        this.value = value;
    }

    public static Action fromString(String value) {
        for (Action action : Action.values()) {
            if (action.value.equalsIgnoreCase(value)) {
                return action;
            }
        }
        return null;
    }
}
