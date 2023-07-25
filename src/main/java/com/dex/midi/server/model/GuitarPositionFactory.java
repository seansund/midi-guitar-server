package com.dex.midi.server.model;

import com.dex.midi.model.GuitarPosition;
import com.dex.midi.model.GuitarPositions;

import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class GuitarPositionFactory {
    public static GuitarPositions fromFrets(Integer ...frets) {
        final GuitarPosition[] positions = IntStream
                .range(0, frets.length)
                .mapToObj(GuitarPositionFactory.positionFromFrets(frets))
                .toList()
                .toArray(new GuitarPosition[frets.length]);

        return new GuitarPositions(positions);
    }

    public static IntFunction<GuitarPosition> positionFromFrets(Integer ...frets) {
        return (int stringIndex) -> new GuitarPosition(stringIndex, frets[stringIndex]);
    }
}
