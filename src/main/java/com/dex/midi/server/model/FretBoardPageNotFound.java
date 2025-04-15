package com.dex.midi.server.model;

import lombok.Getter;

@Getter
public class FretBoardPageNotFound extends RuntimeException {
    private final String mode;
    private final KeyboardPage page;
    private final KeyboardShift shift;

    public FretBoardPageNotFound(String mode, KeyboardPage page, KeyboardShift shift) {
        super("Unable to find page for " + mode + " mode: " + page + ", " + shift);

        this.mode = mode;
        this.page = page;
        this.shift = shift;
    }
}
