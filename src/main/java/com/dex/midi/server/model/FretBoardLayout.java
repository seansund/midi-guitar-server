package com.dex.midi.server.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class FretBoardLayout extends FretBoardMode {
    private GuitarKey key;
    @NonNull private List<FretBoardLayoutPage> pages = new ArrayList<>();
    private String underlay;
    private Boolean keyboard;

    public FretBoardLayout() {
        super();
    }

    public FretBoardLayout(@NonNull String mode, @NonNull String label) {
        this(mode, label, new ArrayList<>());
    }

    public FretBoardLayout(@NonNull String mode, @NonNull String label, GuitarKey key) {
        this(mode, label, new ArrayList<>(), key);
    }

    public FretBoardLayout(@NonNull String mode, @NonNull String label, String key) {
        this(mode, label, GuitarKey.lookup(key));
    }

    public FretBoardLayout(@NonNull String mode, @NonNull String label, @NonNull List<FretBoardLayoutPage> pages) {
        this(mode, label, pages, (GuitarKey)null);
    }

    public FretBoardLayout(@NonNull String mode, @NonNull String label, @NonNull List<FretBoardLayoutPage> pages, GuitarKey key) {
        super(mode, label);

        this.pages = pages;
        this.key = key;
    }

    public FretBoardLayout(@NonNull String mode, @NonNull String label, @NonNull List<FretBoardLayoutPage> pages, String key) {
        this(mode, label, pages, GuitarKey.lookup(key));
    }

    public FretBoardLabels getFretBoardLabels(KeyboardPage pageId, KeyboardShift shift) {
        final FretBoardLayoutPage currentPage = pages.stream()
                .filter(page -> page.matchModifiers(pageId, shift))
                .findFirst()
                .orElseThrow(() -> new FretBoardPageNotFound(getMode(), pageId, shift));

        return currentPage.getLabels();
    }

    public void setGuitarKey(String key) {
        this.key = GuitarKey.lookup(key);
    }

    public FretBoardLayout withPages(List<FretBoardLayoutPage> pages) {
        this.pages = pages != null ? pages : new ArrayList<>();

        return this;
    }
}
