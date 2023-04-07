package com.athisii.mhb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * @author athisii
 * @version 1.0
 * @since 05/02/23
 */
@Entity(tableName = "hymn_verse", foreignKeys = @ForeignKey(entity = Hymn.class, parentColumns = "id", childColumns = "hymn_id", onDelete = ForeignKey.CASCADE))
public class HymnVerse implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;
    @ColumnInfo(name = "verse_number", defaultValue = "0")
    @SerializedName("vn")
    private byte verseNumber;
    @ColumnInfo(name = "is_chorus")
    @SerializedName("ic")
    private boolean isChorus;
    @ColumnInfo(name = "hymn_id")
    private int hymnId;
    @Ignore
    @SerializedName("hVLs")
    List<HymnVerseLine> hymnVerseLines;

    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
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
        this.isChorus = chorus;
    }

    public int getHymnId() {
        return hymnId;
    }

    public void setHymnId(int hymnId) {
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
