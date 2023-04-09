package com.athisii.mhb.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "bible_book_chapter", foreignKeys = @ForeignKey(entity = BibleBook.class, parentColumns = "id", childColumns = "bible_id", onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE))
public class BibleBookChapter implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "chapter_number", defaultValue = "0")
    private short chapterNumber;
    @ColumnInfo(name = "bible_id", defaultValue = "0", index = true)
    private long bibleId;

    // To be used only when data is fetched from the server.
    @Ignore
    private List<BibleBookChapterVerse> verses;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public short getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(short chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public long getBibleId() {
        return bibleId;
    }

    public void setBibleId(long bibleId) {
        this.bibleId = bibleId;
    }

    public List<BibleBookChapterVerse> getVerses() {
        return verses;
    }

    public void setVerses(List<BibleBookChapterVerse> verses) {
        this.verses = verses;
    }
}
