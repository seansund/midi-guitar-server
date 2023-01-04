package com.dex.midi.server.model;

public class FretBoardPageNotFound extends RuntimeException {
    private String mode;
    private KeyboardPage page;
    private KeyboardShift shift;

    public FretBoardPageNotFound(String mode, KeyboardPage page, KeyboardShift shift) {
        super("Unable to find page for " + mode + " mode: " + page + ", " + shift);

        this.mode = mode;
        this.page = page;
        this.shift = shift;
    }
}
