package com.athisii.mhb.database;

import androidx.paging.PagingSource;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.athisii.mhb.entity.Hymn;
import com.athisii.mhb.entity.HymnVerse;
import com.athisii.mhb.entity.HymnVerseLine;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

@Dao
public interface HymnDao {

    @Query("SELECT * FROM hymn ORDER BY hymn_number")
    PagingSource<Integer, Hymn> getPaginatedHymns();

    @Query("SELECT * FROM hymn_verse WHERE hymn_id = :hymnId")
    List<HymnVerse> getHymnVerses(long hymnId);

    @Query("SELECT * FROM hymn_verse_line WHERE hymn_verse_id = :hymnVerseId")
    List<HymnVerseLine> getHymnVerseLine(long hymnVerseId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHymn(Hymn hymn);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertHymnVerse(HymnVerse hymnVerse);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHymnVerseLine(List<HymnVerseLine> hymnVerseLines);

    @Transaction
    default void insertHymns(List<Hymn> hymns) {
        if (!hymns.isEmpty()) {
            for (Hymn hymn : hymns) {
                long hymnId = insertHymn(hymn);
                for (HymnVerse hymnVerse : hymn.getHymnVerses()) {
                    hymnVerse.setHymnId(hymnId);
                    long hymnVerseId = insertHymnVerse(hymnVerse);
                    for (HymnVerseLine hymnVerseLine : hymnVerse.getHymnVerseLines()) {
                        hymnVerseLine.setHymnVerseId(hymnVerseId);
                    }
                    insertHymnVerseLine(hymnVerse.getHymnVerseLines());
                }
            }
        }
    }

    @Transaction
    default SortedMap<HymnVerse, List<HymnVerseLine>> getHymnContentById(long hymnId) {
        SortedMap<HymnVerse, List<HymnVerseLine>> hymnContentMap = new TreeMap<>();
        List<HymnVerse> hymnVerses = getHymnVerses(hymnId);
        for (HymnVerse hymnVerse : hymnVerses) {
            hymnContentMap.put(hymnVerse, getHymnVerseLine(hymnVerse.getId()));
        }
        return hymnContentMap;
    }


    @Query("SELECT * FROM hymn WHERE hymn_number LIKE :search OR maola_title LIKE :search " +
            "OR english_title LIKE :search ORDER BY hymn_number")
    PagingSource<Integer, Hymn> searchHymn(String search);
}
