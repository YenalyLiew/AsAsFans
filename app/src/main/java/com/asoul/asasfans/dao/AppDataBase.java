package com.asoul.asasfans.dao;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.asoul.asasfans.entity.HistorySearchEntity;

@Database(entities = {HistorySearchEntity.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    private static AppDataBase sAppDataBase;

    public abstract HistorySearchDao getHistorySearchDao();

    public static AppDataBase getInstance(Context context) {
        if (sAppDataBase == null) {
            synchronized (AppDataBase.class) {
                if (sAppDataBase == null) {
                    sAppDataBase = Room.databaseBuilder(context.getApplicationContext(), AppDataBase.class, "history_entity.db")
                            .allowMainThreadQueries()
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                }

                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                }

                                @Override
                                public void onDestructiveMigration(@NonNull SupportSQLiteDatabase db) {
                                    super.onDestructiveMigration(db);
                                }
                            })
                            .build();
                }
            }
        }
        return sAppDataBase;
    }
}
