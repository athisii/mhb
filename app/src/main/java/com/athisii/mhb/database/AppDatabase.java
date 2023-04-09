package com.athisii.mhb.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.athisii.mhb.database.dao.BibleBookChapterDao;
import com.athisii.mhb.database.dao.BibleBookChapterVerseDao;
import com.athisii.mhb.database.dao.BibleBookDao;
import com.athisii.mhb.database.dao.HymnDao;
import com.athisii.mhb.database.dao.HymnVerseDao;
import com.athisii.mhb.database.dao.HymnVerseLineDao;
import com.athisii.mhb.entity.BibleBook;
import com.athisii.mhb.entity.BibleBookChapter;
import com.athisii.mhb.entity.BibleBookChapterVerse;
import com.athisii.mhb.entity.Hymn;
import com.athisii.mhb.entity.HymnVerse;
import com.athisii.mhb.entity.HymnVerseLine;

@Database(entities = {Hymn.class, HymnVerse.class, HymnVerseLine.class, BibleBook.class, BibleBookChapter.class, BibleBookChapterVerse.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract HymnDao hymnDao();

    public abstract HymnVerseDao hymnVerseDao();

    public abstract HymnVerseLineDao hymnVerseLineDao();

    public abstract BibleBookDao bibleBookDao();

    public abstract BibleBookChapterDao bibleBookChapterDao();

    public abstract BibleBookChapterVerseDao bibleBookChapterVerseDao();

}
