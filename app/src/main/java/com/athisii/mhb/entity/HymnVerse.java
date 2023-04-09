package com.athisii.mhb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

/**
 * @author athisii
 * @version 1.0
 * @since 05/02/23
 */
@Entity(tableName = "hymn_verse", foreignKeys = @ForeignKey(entity = Hymn.class, parentColumns = "id", childColumns = "hymn_id", onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE))
public class HymnVerse implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "verse_number", defaultValue = "0")
    private byte verseNumber;
    @ColumnInfo(name = "is_chorus")
    private boolean isChorus;
    @ColumnInfo(name = "hymn_id", index = true)
    private long hymnId;

    // To be used only when data is fetched from the server.
    @Ignore
    private List<HymnVerseLine> hymnVerseLines;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte getVerseNumber() {
        return verseNumber;
    }

    public void setVerseNumber(byte verseNumber) {
        this.verseNumber = verseNumber;
    }

    public boolean isChorus() {
        return isChorus;
    }

    public void setChorus(boolean chorus) {
        isChorus = chorus;
    }

    public long getHymnId() {
        return hymnId;
    }

    public void setHymnId(long hymnId) {
        this.hymnId = hymnId;
    }

    public List<HymnVerseLine> getHymnVerseLines() {
        return hymnVerseLines;
    }

    public void setHymnVerseLines(List<HymnVerseLine> hymnVerseLines) {
        this.hymnVerseLines = hymnVerseLines;
    }

    @NonNull
    @Override
    public String toString() {
        return "HymnVerse{" +
                "id=" + id +
                ", verseNumber=" + verseNumber +
                ", isChorus=" + isChorus +
                ", hymnId=" + hymnId +
                ", hymnVerseLines=" + hymnVerseLines +
                '}';
    }
}
