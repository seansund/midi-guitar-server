package com.dex.midi.server.model;

import com.dex.midi.model.GuitarPosition;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Arrays;

@Data
@EqualsAndHashCode(callSuper = true)
public class ActiveGuitarPosition extends GuitarPosition {
    private boolean active;
    private boolean bend;

    public ActiveGuitarPosition() {
        super();

        this.active = false;
        this.bend = false;
    }

    public ActiveGuitarPosition(GuitarPosition position) {
        this(position.getStringIndex(), position.getFretIndex(), true);
    }

    public ActiveGuitarPosition(int stringIndex, int fretIndex) {
        this(stringIndex, fretIndex, true);
    }

    public ActiveGuitarPosition(int stringIndex, int fretIndex, boolean active) {
        super(stringIndex, fretIndex);
        this.active = active;
    }

    public static ActiveGuitarPosition from(GuitarPosition position) {
        if (position == null) {
            return null;
        }

        return new ActiveGuitarPosition(position);
    }

    public static ActiveGuitarPosition[] fromArray(GuitarPosition[] positions) {
        return Arrays.stream(positions)
                .map(ActiveGuitarPosition::from)
                .toList()
                .toArray(new ActiveGuitarPosition[positions.length]);
    }
}
