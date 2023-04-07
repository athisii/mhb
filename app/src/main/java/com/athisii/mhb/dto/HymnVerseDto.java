package com.athisii.mhb.dto;

import java.util.LinkedList;

public class HymnVerseDto {
    // line separated by new-line character
    private boolean isChorus;
    private LinkedList<String> lines;

    public boolean isChorus() {
        return isChorus;
    }

    public void setChorus(boolean chorus) {
        isChorus = chorus;
    }

    public LinkedList<String> getLines() {
        return lines;
    }

    public void setLines(LinkedList<String> lines) {
        this.lines = lines;
    }
}
