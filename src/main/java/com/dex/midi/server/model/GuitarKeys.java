package com.dex.midi.server.model;

import java.util.Arrays;
import java.util.List;

public class GuitarKeys {
    private static GuitarKeys _instance;

    protected GuitarKeys() {
        super();
    }

    public static GuitarKeys instance() {
        if (_instance == null) {
            _instance = new GuitarKeys();
        }

        return _instance;
    }

    public static GuitarKey defaultKey() {
        return defaultKey;
    }

    public static GuitarKey lookupKey(String key) {
        return GuitarKeys.instance().lookup(key);
    }

    private static final GuitarKey defaultKey = GuitarKey.of("G", "G / e");

    private final List<GuitarKey> keys = Arrays.asList(
            defaultKey,
            GuitarKey.of("G#", "G# / f"),
            GuitarKey.of("A", "A / f#"),
            GuitarKey.of("A#", "A# / g"),
            GuitarKey.of("B", "B / g#"),
            GuitarKey.of("C", "C / a"),
            GuitarKey.of("C#", "C# / a#"),
            GuitarKey.of("D", "D / b"),
            GuitarKey.of("D#", "D# / c"),
            GuitarKey.of("E", "E / c#"),
            GuitarKey.of("F", "F / d"),
            GuitarKey.of("F#", "F# / d#")
    );

    public GuitarKey getDefaultKey() {
        return defaultKey;
    }

    public List<GuitarKey> getKeys() {
        return keys;
    }

    public GuitarKey lookup(String key) {
        return keys.stream()
                .filter((GuitarKey value) -> value.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new GuitarKeyNotFound(key));
    }
}
