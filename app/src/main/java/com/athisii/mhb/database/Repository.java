package com.athisii.mhb.database;

import android.widget.Toast;

import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;
import androidx.room.Room;

import com.athisii.mhb.App;
import com.athisii.mhb.entity.BibleBook;
import com.athisii.mhb.entity.Hymn;
import com.athisii.mhb.entity.HymnVerse;
import com.athisii.mhb.entity.HymnVerseLine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Flowable;

public class Repository {
    int size;
    private static final String DB_NAME = "app_db";
    public static final int PAGE_SIZE = 13;
    private static final Gson GSON = new Gson();


    public enum FileType {
        HYMN("hymn_data.json"),
        BIBLE("bible_data.json");
        private final String value;

        FileType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private final App application;
    private final AppDatabase database;
    private static Repository repository;

    // Singleton pattern
    private Repository(App application) {
        this.application = application;
        database = Room.databaseBuilder(application, AppDatabase.class, DB_NAME).build();
    }

    public static synchronized Repository getInstance(App application) {
        if (repository == null) {
            repository = new Repository(application);
        }
        return repository;
    }

    public Flowable<PagingData<Hymn>> getPagingDataFlow() {
        Pager<Integer, Hymn> pager = new Pager<>(new PagingConfig(PAGE_SIZE, 39, false, 39, PagingConfig.MAX_SIZE_UNBOUNDED), 0, () -> database.hymnDao().getPaginatedHymns());
        return PagingRx.getFlowable(pager);
    }

    public Map<HymnVerse, List<HymnVerseLine>> getHymnContentById(long id) {
        return database.hymnDao().getHymnContentById(id);
    }

    public void saveDataToDb(FileType fileType) {
        String jsonString;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(application.getAssets().open(fileType.getValue())))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            jsonString = stringBuilder.toString();
        } catch (IOException e) {
            application.getHandler().post(() -> Toast.makeText(application, "Something went wrong.", Toast.LENGTH_LONG).show());
            return;
        }

        if (FileType.HYMN == fileType) {
            List<Hymn> hymns = GSON.fromJson(jsonString, new TypeToken<List<Hymn>>() {
            }.getType());
            insertHymn(hymns);
        } else {
            List<BibleBook> bibleBooks = GSON.fromJson(jsonString, new TypeToken<List<BibleBook>>() {
            }.getType());
            insertBibleBook(bibleBooks);
        }
    }

    public void insertHymn(List<Hymn> hymns) {
        database.hymnDao().insertHymns(hymns);
    }

    public void insertBibleBook(List<BibleBook> bibleBooks) {
        if (!bibleBooks.isEmpty()) {
            database.bibleBookDao().insertBibleBook(bibleBooks);
        }
    }

    public Flowable<PagingData<Hymn>> searchHymn(String query) {
        Pager<Integer, Hymn> pager = new Pager<>(new PagingConfig(PAGE_SIZE, 39, false, 39, PagingConfig.MAX_SIZE_UNBOUNDED), 0, () -> database.hymnDao().searchHymn(query));
        return PagingRx.getFlowable(pager);
    }

}
