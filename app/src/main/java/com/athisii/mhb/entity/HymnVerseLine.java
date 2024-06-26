package com.athisii.mhb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;


/**
 * @author athisii
 * @version 1.0
 * @since 05/02/23
 */
@Entity(tableName = "hymn_verse_line", foreignKeys = @ForeignKey(entity = HymnVerse.class, parentColumns = "id", childColumns = "hymn_verse_id", onUpdate = ForeignKey.CASCADE, onDelete = ForeignKey.CASCADE))
public class HymnVerseLine implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "serial_number", defaultValue = "0")
    private byte serialNumber;
    @ColumnInfo(name = "maola", defaultValue = "NA")
    private String maola;
    @ColumnInfo(name = "english", defaultValue = "NA")
    private String english;
    @ColumnInfo(name = "hymn_verse_id", index = true)
    private long hymnVerseId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public byte getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(byte serialNumber) {
        this.serialNumber = serialNumber;
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

    public long getHymnVerseId() {
        return hymnVerseId;
    }

    public void setHymnVerseId(long hymnVerseId) {
        this.hymnVerseId = hymnVerseId;
    }

    @NonNull
    @Override
    public String toString() {
        return "HymnVerseLine{" +
                "id=" + id +
                ", serialNumber=" + serialNumber +
                ", maola='" + maola + '\'' +
                ", english='" + english + '\'' +
                ", hymnVerseId=" + hymnVerseId +
                '}';
    }
}
