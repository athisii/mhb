package com.athisii.mhb.dto;

import java.util.LinkedList;
import java.util.List;

public class HymnResDto {
    private short number;
    private String maolaTitle;
    private String englishTitle;
    // chorus is always zero-indexed if present
    private LinkedList<HymnVerseDto> verses;

    public short getNumber() {
        return number;
    }

    public void setNumber(short number) {
        this.number = number;
    }

    public String getMaolaTitle() {
        return maolaTitle;
    }

    public void setMaolaTitle(String maolaTitle) {
        this.maolaTitle = maolaTitle;
    }

    public String getEnglishTitle() {
        return englishTitle;
    }

    public void setEnglishTitle(String englishTitle) {
        this.englishTitle = englishTitle;
    }

    public LinkedList<HymnVerseDto> getVerses() {
        return verses;
    }

    public void setVerses(LinkedList<HymnVerseDto> verses) {
        this.verses = verses;
    }
}
