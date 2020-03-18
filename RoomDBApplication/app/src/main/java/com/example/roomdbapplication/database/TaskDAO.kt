package com.example.roomdbapplication.database

import android.util.Log
import androidx.room.*

@Dao
interface TaskDAO {
    @Insert
    suspend fun insertTask(task: Task)

    @Query("select * from Task order by tID desc")
    suspend fun getAllTask(): List<Task>

    @Update
    fun updateTask(task: Task)
    /*@Delete()
    fun deleteTask(tID: Int)*/
}

/*
    * we have to use suspend keyword before fun,
      to execute in coroutines blocks.
    * And when the function is suspend,
      we can't call it directly.
     */