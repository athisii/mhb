package com.athisii.mhb.database;

import android.widget.Toast;

import androidx.room.Room;

import com.athisii.mhb.App;
import com.athisii.mhb.entity.BibleBook;
import com.athisii.mhb.entity.Hymn;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Repository {
    int size;
    private static final String DB_NAME = "app_db";
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

}