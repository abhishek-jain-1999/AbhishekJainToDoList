package com.example.abhishek.mytodolist;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface TodoDAO {


    @Insert
    Long insertData(TaskData taskData);

    @Delete
    int delData(TaskData taskData);

    @Update()
    void updateData(TaskData taskData);

    @Query("select * from todolist")
    List<TaskData> getWholeData();

    @Query("select * from todolist where name = :nameA")
    TaskData getTaskDataSingle(String nameA);


}
