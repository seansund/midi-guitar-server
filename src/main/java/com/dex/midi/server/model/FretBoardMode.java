package com.dex.midi.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class FretBoardMode {
    @NonNull private String mode;
    @NonNull private String label;

    public static FretBoardMode of(String mode, String label) {
        return new FretBoardMode(mode, label);
    }
}
