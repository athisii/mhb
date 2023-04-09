package com.athisii.mhb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "hymn", indices = {@Index(name = "hymn_number", value = {"hymn_number"}, unique = true)})
public class Hymn implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "hymn_number", defaultValue = "0")
    private short hymnNumber;
    @ColumnInfo(name = "is_favourite", index = true)
    private boolean isFavorite;
    @ColumnInfo(name = "maola_title", index = true, defaultValue = "NA")
    private String maolaTitle;

    @ColumnInfo(name = "english_title", index = true, defaultValue = "NA")
    private String englishTitle;

    // To be used only when data is fetched from the server.
    @Ignore
    private List<HymnVerse> hymnVerses;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public short getHymnNumber() {
        return hymnNumber;
    }

    public void setHymnNumber(short hymnNumber) {
        this.hymnNumber = hymnNumber;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
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

    public List<HymnVerse> getHymnVerses() {
        return hymnVerses;
    }

    public void setHymnVerses(List<HymnVerse> hymnVerses) {
        this.hymnVerses = hymnVerses;
    }

    @NonNull
    @Override
    public String toString() {
        return "Hymn{" +
                "id=" + id +
                ", hymnNumber=" + hymnNumber +
                ", isFavorite=" + isFavorite +
                ", maolaTitle='" + maolaTitle + '\'' +
                ", englishTitle='" + englishTitle + '\'' +
                ", hymnVerses=" + hymnVerses +
                '}';
    }
}
