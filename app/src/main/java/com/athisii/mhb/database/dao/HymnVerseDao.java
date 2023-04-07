package com.athisii.mhb.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.athisii.mhb.entity.HymnVerse;
import com.athisii.mhb.entity.HymnVerseLine;

import java.util.List;
import java.util.Map;

@Dao
public interface HymnVerseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<HymnVerse> hymnVerse);

    @Query("SELECT * FROM hymn_verse JOIN hymn_verse_line ON hymn_verse.id = hymn_verse_line.hymn_verse_id")
    Map<HymnVerse, List<HymnVerseLine>> loadVerse();
}
