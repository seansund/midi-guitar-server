package com.dex.midi.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public enum GuitarKey {
    G("G", "G / e"),
    Ab("Ab", "Ab / f"),
    A("A", "A / f#"),
    Bb("Bb", "Bb / g"),
    B("B", "B / g#"),
    C("C", "C / a"),
    Db("Db", "Db / a#"),
    D("D", "D / b"),
    Eb("Eb", "Eb / c"),
    E("E", "E / c#"),
    F("F", "F / d"),
    Gb("Gb", "Gb / d#");

    private String key;
    private String label;

    GuitarKey(String key, String label) {
        this.key = key;
        this.label = label;
    }

    public String getKey() {
        return key;
    }

    public String getLabel() {
        return label;
    }

    public static List<GuitarKey> getKeys() {
        return Arrays.asList(GuitarKey.values());
    }

    public static GuitarKey lookup(String key) {
        if (key == null) {
            return null;
        }

        return Stream.of(values())
                .filter(val -> val.key.equals(key))
                .findFirst()
                .orElseThrow(() -> new GuitarKeyNotFound(key));
    }

    public static GuitarKey defaultKey() {
        return G;
    }

    public int transposeKey(GuitarKey newKey) {
        if (this.equals(newKey) || newKey == null) {
            return 0;
        }

        final List<GuitarKey> values = Arrays.asList(values());

        return values.indexOf(newKey) - values.indexOf(this);
    }
}
