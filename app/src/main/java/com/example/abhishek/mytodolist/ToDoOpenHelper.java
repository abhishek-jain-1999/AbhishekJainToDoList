package com.example.abhishek.mytodolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ToDoOpenHelper extends SQLiteOpenHelper {
    private static ToDoOpenHelper OpenHelper;

    public static ToDoOpenHelper getOpenHelper(Context context) {
        if(OpenHelper==null){
            OpenHelper=new ToDoOpenHelper(context.getApplicationContext());
        }
        return OpenHelper;
    }

    private ToDoOpenHelper(Context context) {

        super(context, "todolist", null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String todosql1="Create Table todolistable (id Integer primary key Autoincrement ,title Text,data TEXT,date TEXT )";
        sqLiteDatabase.execSQL(todosql1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
