package com.athisii.mhb.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import com.athisii.mhb.entity.HymnVerseLine;

import java.util.List;

@Dao
public interface HymnVerseLineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<HymnVerseLine> hymnVerseLines);
}
