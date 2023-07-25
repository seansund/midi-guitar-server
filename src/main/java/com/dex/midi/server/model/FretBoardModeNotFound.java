package com.dex.midi.server.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
public class FretBoardModeNotFound extends RuntimeException {
    private String mode;

    public FretBoardModeNotFound(String mode) {
        super("Fret board mode not found: " + mode);

        this.mode = mode;
    }
}
