package com.dex.midi.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
public class FretBoardConfig {
    @NonNull private String key;
    @NonNull private String mode;
}
