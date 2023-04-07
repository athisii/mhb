package com.athisii.mhb.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.athisii.mhb.database.dao.HymnDao;
import com.athisii.mhb.database.dao.HymnVerseDao;
import com.athisii.mhb.database.dao.HymnVerseLineDao;
import com.athisii.mhb.entity.Hymn;
import com.athisii.mhb.entity.HymnVerse;
import com.athisii.mhb.entity.HymnVerseLine;

@Database(entities = {Hymn.class, HymnVerse.class, HymnVerseLine.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HymnDao hymnDao();
    public abstract HymnVerseDao hymnVerseDao();
    public abstract HymnVerseLineDao hymnVerseLineDao();

}
