package com.dex.midi.server.model;

import com.dex.midi.model.GuitarPosition;
import lombok.Data;
import lombok.NonNull;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

@Data
public class FretBoardLabel implements GuitarPositionIF {
    private Integer stringIndex;
    private Integer fretIndex;
    private String label;

    public FretBoardLabel() {
        super();
    }

    public FretBoardLabel(int stringIndex, int fretIndex, String label) {
        this.stringIndex = stringIndex;
        this.fretIndex = fretIndex;
        this.label = label;
    }

    public static Predicate<FretBoardLabel> byGuitarPosition(@NonNull GuitarPosition pos) {
        return label -> label.forGuitarPosition(pos);
    }

    public static Function<FretBoardLabel, FretBoardLabel> withTransposer(int transpose) {
        return label -> label.transpose(transpose);
    }

    public boolean forGuitarPosition(@NonNull GuitarPosition pos) {
        return pos.getStringIndex() == stringIndex && pos.getFretIndex() == fretIndex;
    }

    public boolean forGuitarPosition(@NonNull GuitarPositionIF pos) {
        return Objects.equals(pos.getStringIndex(), stringIndex) && Objects.equals(pos.getFretIndex(), fretIndex);
    }

    public FretBoardLabel transpose(int transposeDistance) {
        if (transposeDistance == 0) {
            return this;
        }

        final int absTransposeDistance = (transposeDistance + 12) % 12;

        final int newFretIndex = (fretIndex + absTransposeDistance) % FretBoardConstants.FRET_COUNT;

        return new FretBoardLabel(stringIndex, newFretIndex, label);
    }
}
