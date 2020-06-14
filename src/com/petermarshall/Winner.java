package com.petermarshall;

public enum Winner {
    HOME(0),
    DRAW(1),
    AWAY(2);

    private final int setting;

    Winner(int setting) {
        this.setting = setting;
    }

    public int getSetting() {
        return setting;
    }
}
