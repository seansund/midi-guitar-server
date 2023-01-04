package com.dex.midi.server.model;

import com.dex.midi.event.PitchEvent;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FretBoardLayoutPage {
    private KeyboardPage page;
    private KeyboardShift shift;
    private String file;
    private FretBoardLabels labels;

    public boolean matchModifiers(KeyboardPage pageId, KeyboardShift shiftId) {
        return (page == null && shift == null) ||
                (pageId == page && shiftId == shift);
    }

    public KeyboardPage getPage() {
        return page;
    }
    public void setPage(String page) {
        this.page = KeyboardPage.lookup(page);
    }
    public void setPage(KeyboardPage page) {
        this.page = page;
    }

    public KeyboardShift getShift() {
        return shift;
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
