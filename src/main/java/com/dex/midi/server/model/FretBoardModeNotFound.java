package com.dex.midi.server.model;

import lombok.Getter;

@Getter
public class FretBoardModeNotFound extends RuntimeException {
    private final String mode;

    public FretBoardModeNotFound(String mode) {
        super("Fret board mode not found: " + mode);

        this.mode = mode;
    }
}
