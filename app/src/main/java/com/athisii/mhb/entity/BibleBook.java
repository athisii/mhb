package com.athisii.mhb.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "bible_book")
public class BibleBook implements Serializable {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;
    @ColumnInfo(name = "maola_name", defaultValue = "NA", index = true)
    private String maolaName;
    @ColumnInfo(name = "english_name", defaultValue = "NA", index = true)
    private String englishName;

    // To be used only when data is loaded from the file.
    @Ignore
    List<BibleBookChapter> chapters;

    @NonNull
    public Integer getId() {
        return id;
    }

    public void setId(@NonNull Integer id) {
        this.id = id;
    }

    public String getMaolaName() {
        return maolaName;
    }

    public void setMaolaName(String maolaName) {
        this.maolaName = maolaName;
    }

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public List<BibleBookChapter> getChapters() {
        return chapters;
    }

    public void setChapters(List<BibleBookChapter> chapters) {
        this.chapters = chapters;
    }
}
