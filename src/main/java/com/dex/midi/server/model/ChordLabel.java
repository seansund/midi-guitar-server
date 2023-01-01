package com.dex.midi.server.model;

import com.dex.midi.chord.Chord;
import lombok.Data;

import java.util.List;

@Data
public class ChordLabel {
    private String label;

    public ChordLabel() {
        this("");
    }

    public ChordLabel(String label) {
        this.label = label;
    }

    public ChordLabel(Chord chord) {
        this(chord.toString());
    }

    public static List<ChordLabel> from(List<Chord> chords) {
        return chords.stream().map(ChordLabel::new).toList();
    }
}
