package com.dex.midi.server.model;

import java.util.Arrays;
import java.util.List;

public class FretBoardModes {
    private static FretBoardModes _instance;

    protected FretBoardModes() {
        super();
    }

    public static FretBoardModes instance() {
        if (_instance == null) {
            _instance = new FretBoardModes();
        }

        return _instance;
    }

    public static FretBoardMode defaultMode() {
        return defaultMode;
    }

    public static FretBoardMode lookupMode(String mode) {
        return FretBoardModes.instance().lookup(mode);
    }

    private static final FretBoardMode defaultMode = FretBoardMode.of("notes", "Notes");

    private final List<FretBoardMode> modes = Arrays.asList(
            defaultMode,
            FretBoardMode.of("keyboard-major", "Keyboard Major Scale"),
            FretBoardMode.of("keyboard-pentatonic", "Keyboard Pentatonic"),
            FretBoardMode.of("keyboard-harmonic-minor", "Keyboard Harmonic Mino")
    );

    public FretBoardMode getDefaultMode() {
        return defaultMode;
    }

    public List<FretBoardMode> getModes() {
        return modes;
    }

    public FretBoardMode lookup(String mode) {
        return modes.stream()
                .filter((FretBoardMode value) -> value.getMode().equals(mode))
                .findFirst()
                .orElseThrow(() -> new FretBoardModeNotFound(mode));
    }
}
