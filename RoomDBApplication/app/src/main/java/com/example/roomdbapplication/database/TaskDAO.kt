package com.example.roomdbapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDAO {

    @Insert
    fun insertTask(task: Task)

    @Query("select * from Task")
    fun getAllTask(): List<Task>

    /*@Delete()
    fun deleteTask(tID: Int)*/
}