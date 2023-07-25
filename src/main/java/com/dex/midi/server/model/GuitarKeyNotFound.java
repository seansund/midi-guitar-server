package com.dex.midi.server.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
public class GuitarKeyNotFound extends RuntimeException {
    private String key;

    public GuitarKeyNotFound(String key) {
        super("Guitar key not found: " + key);

        this.key = key;
    }
}
