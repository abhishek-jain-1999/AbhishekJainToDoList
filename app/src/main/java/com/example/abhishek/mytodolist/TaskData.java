package com.example.abhishek.mytodolist;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Calendar;

@Entity(tableName = "todolist")
public class TaskData {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;
    public String taskString;
    public String dateSet, timeset;
    @Ignore
    public String epochTime;


    public void setName(String name) {
        this.name = name;
    }

    public void setTaskString(String taskString) {
        this.taskString = taskString;
    }

    public String getName() {

        return name;
    }

    public void setDateSet(int day, int month, int year) {
        this.dateSet = "" + day + "/" + month + "/" + year;
    }

    public String getTaskString() {
        return taskString;
    }

    TaskData(String name, String taskString) {
        this.name = name;
        this.taskString = taskString;

    }


}
