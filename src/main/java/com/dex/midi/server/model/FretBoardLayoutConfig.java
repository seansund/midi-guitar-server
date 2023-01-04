package com.dex.midi.server.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class FretBoardLayoutConfig {
    private String defaultMode;
    private List<FretBoardLayout> modes = new ArrayList<>();
}
