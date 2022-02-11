package com.daclink.drew.sp22.cst438_project01_starter;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {User.class}, version = 2)
public abstract class UserDb extends RoomDatabase {
    private static UserDb instance; // singleton pattern
    public static final String DATABASE_NAME = "person_db";
    public abstract UserDAO getPersonDAO();
    public static synchronized UserDb getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    UserDb.class,
                    DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}

