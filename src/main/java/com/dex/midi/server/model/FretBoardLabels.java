package com.dex.midi.server.model;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
public class FretBoardLabels {
    private GuitarKey baseKey;
    private List<FretBoardLabel> labels;

    public FretBoardLabels() {
        this(new ArrayList<>());
    }

    public FretBoardLabels(List<FretBoardLabel> labels) {
        this((GuitarKey)null, labels);
    }

    public FretBoardLabels(String baseKey, List<FretBoardLabel> labels) {
        this(GuitarKey.lookup(baseKey), labels);
    }

    public FretBoardLabels(GuitarKey baseKey, List<FretBoardLabel> labels) {
        this.baseKey = baseKey;
        this.labels = labels != null ? labels : new ArrayList<>();
    }

    public static FretBoardLabels fromFrets(GuitarKey baseKey, List<List<String>> frets) {
        final List<FretBoardLabel> labels = new ArrayList<>();

        for (int fretIndex = 0; fretIndex < frets.size(); fretIndex++) {
            List<String> fretValues = frets.get(fretIndex);

            for (int i = 0; i < Math.min(fretValues.size(), 6); i++) {
                final int stringIndex = 5 - i;

                final String label = fretValues.get(i);

                if (!label.isEmpty()) {
                    labels.add(new FretBoardLabel(stringIndex, fretIndex, label));
                }
            }
        }

        return new FretBoardLabels(baseKey, labels);
    }

    public FretBoardLabels transposeKey(String newKey) {
        return transposeKey(GuitarKey.lookup(newKey));
    }

    public FretBoardLabels transposeKey(GuitarKey newKey) {
        if (baseKey == null || baseKey.equals(newKey)) {
            return this;
        }

        final int transposeDistance = baseKey.transposeKey(newKey);
        System.out.println("Transpose distance: " + transposeDistance);

        final List<FretBoardLabel> newLabels = labels
                .stream()
                .map(FretBoardLabel.withTransposer(transposeDistance))
                .toList();

        return new FretBoardLabels(baseKey, newLabels);
    }

    public FretBoardLabels applyUnderlay(@NonNull FretBoardLabels underlay) {
        return new FretBoardLabels(
                this.baseKey,
                labels.stream()
                        .map(underlay::lookup)
                        .filter(Objects::nonNull)
                        .toList()
        );
    }

    public FretBoardLabel lookup(GuitarPositionIF pos) {
        return labels.stream()
                .filter(label -> label.forGuitarPosition(pos))
                .findFirst()
                .orElse(null);
    }
}
