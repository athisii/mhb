package com.athisii.mhb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "bible_book_chapter_verse", foreignKeys = @ForeignKey(entity = BibleBookChapter.class, parentColumns = "id", childColumns = "bible_book_chapter_id", onDelete = ForeignKey.CASCADE))
public class BibleBookChapterVerse implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;
    @ColumnInfo(name = "verse_number", defaultValue = "0")
    private short verseNumber;
    @ColumnInfo(name = "bible_book_chapter_id", defaultValue = "0", index = true)
    private int bibleBookChapterId;
    @ColumnInfo(name = "maola", defaultValue = "NA")
    private String maola;
    @ColumnInfo(name = "english", defaultValue = "NA")
    private String english;

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public short getVerseNumber() {
        return verseNumber;
    }

    public void setVerseNumber(short verseNumber) {
        this.verseNumber = verseNumber;
    }

    public int getBibleBookChapterId() {
        return bibleBookChapterId;
    }

    public void setBibleBookChapterId(int bibleBookChapterId) {
        this.bibleBookChapterId = bibleBookChapterId;
    }

    public String getMaola() {
        return maola;
    }

    public void setMaola(String maola) {
        this.maola = maola;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}
