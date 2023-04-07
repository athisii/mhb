package com.athisii.mhb.database.dao;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.athisii.mhb.entity.Hymn;

import java.util.List;

@Dao
public interface HymnDao {

    @Query("SELECT * FROM hymn ORDER BY hymn_number")
    List<Hymn> getHymns();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHymns(List<Hymn> hymn);

    @Query("SELECT * FROM hymn WHERE hymn_number LIKE :search OR maola_title LIKE :search " +
            "OR english_title LIKE :search")
    List<Hymn> searchHymn(String search);
}
