package com.example.roomdbapplication.database

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDAO {
    @Insert
    suspend fun insertTask(task: Task)

    @Query("select * from Task order by tID desc")
    suspend fun getAllTask(): List<Task>

    /*@Delete()
    fun deleteTask(tID: Int)*/
}

/*
    * we have to use suspend keyword before fun,
      to execute in coroutines blocks.
    * And when the function is suspend,
      we can't call it directly.
     */