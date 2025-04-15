package com.dex.midi.server.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FretBoardLayoutPage {
    @Getter
    private KeyboardPage page;
    @Getter
    private KeyboardShift shift;
    private String file;
    private FretBoardLabels labels;

    public boolean matchModifiers(KeyboardPage pageId, KeyboardShift shiftId) {
        return (page == null && shift == null) ||
                (pageId == page && shiftId == shift);
    }

    public void setPage(String page) {
        this.page = KeyboardPage.lookup(page);
    }
    public void setPage(KeyboardPage page) {
        this.page = page;
    }

    public void setShift(boolean shift) {
        this.shift = KeyboardShift.lookup(shift);
    }
    public void setShift(KeyboardShift shift) {
        this.shift = shift;
    }

    public FretBoardLayoutPage withLabels(FretBoardLabels labels) {
        if (labels == null) {
            labels = new FretBoardLabels();
        }

        this.setLabels(labels);

        return this;
    }
}
