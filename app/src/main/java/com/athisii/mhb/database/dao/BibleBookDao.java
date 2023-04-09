package com.athisii.mhb.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.athisii.mhb.entity.BibleBook;

import java.util.List;

@Dao
public interface BibleBookDao {
    @Query("SELECT * FROM bible_book ORDER BY id")
    List<BibleBook> getAllBibleBooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBibleBook(List<BibleBook> bibleBooks);

    @Query("SELECT * FROM bible_book WHERE maola_name LIKE :search OR english_name LIKE :search")
    List<BibleBook> searchBibleBook(String search);

    @Query("DELETE FROM hymn")
    void deleteBibleBooks();
}
