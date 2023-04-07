package com.athisii.mhb.dto;

import java.util.LinkedList;

public class BibleResDto {
    private String book;
    private LinkedList<BibleChapterDto> chapters;

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public LinkedList<BibleChapterDto> getChapters() {
        return chapters;
    }

    public void setChapters(LinkedList<BibleChapterDto> chapters) {
        this.chapters = chapters;
    }
}
