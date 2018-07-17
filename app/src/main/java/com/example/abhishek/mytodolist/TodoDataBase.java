package com.example.abhishek.mytodolist;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {TaskData.class}, version = 1, exportSchema = false)
public abstract class TodoDataBase extends RoomDatabase {

    private static TodoDataBase db;

    static TodoDataBase getDb(Context context) {

        if (db == null) {
            db = Room.databaseBuilder(context.getApplicationContext(), TodoDataBase.class, "todo_db").allowMainThreadQueries().build();

        }
        return db;

    }


    abstract TodoDAO getTodoDao();
}
