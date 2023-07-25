package com.dex.midi.server.model;

import java.util.Arrays;

public enum KeyboardPage {
    one("one"),
    two("two");

    private final String value;

    KeyboardPage(String value) {
        this.value = value;
    }

    public static KeyboardPage getDefault() {
        return KeyboardPage.one;
    }

    public static KeyboardPage lookup(final String value) {
        return Arrays.stream(KeyboardPage.values())
                .filter(page -> page.value.equals(value))
                .findFirst()
                .orElse(KeyboardPage.getDefault());
    }

    public KeyboardPage reset() {
        return KeyboardPage.getDefault();
    }

    public KeyboardPage next() {
        return this == one ? two : one;
    }

    public boolean isDefault() {
        return this == getDefault();
    }
}
