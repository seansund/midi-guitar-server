package com.dex.midi.server.model;

import java.util.Arrays;

public enum KeyboardShift {
    off(false),
    on(true);

    private final boolean value;

    KeyboardShift(boolean value) {
        this.value = value;
    }

    public static KeyboardShift getDefault() {
        return off;
    }

    public static KeyboardShift lookup(final boolean value) {
        return Arrays.stream(KeyboardShift.values())
                .filter(shift -> shift.value == value)
                .findFirst()
                .orElse(KeyboardShift.getDefault());
    }

    public KeyboardShift reset() {
        return KeyboardShift.getDefault();
    }

    public KeyboardShift next() {
        return this == off ? on : off;
    }

    public boolean isDefault() {
        return this == getDefault();
    }
}
