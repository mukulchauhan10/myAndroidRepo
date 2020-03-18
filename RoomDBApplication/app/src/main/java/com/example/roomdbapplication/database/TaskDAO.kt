package com.example.roomdbapplication.database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDAO {
    @Insert
    suspend fun insertTask(task: Task)

    @Query("select * from Task order by tID desc")
    fun getAllTask(): LiveData<List<Task>>

    @Query("delete from Task where tid = :uid")
    fun deleteTask(uid: Int)

    @Query("update Task set tActivate = 0 where tID = :uid")
    fun updateTask(uid: Int)
    /*@Delete()
    fun deleteTask(tID: Int)*/
}

/*
    * we have to use suspend keyword before fun,
      to execute in coroutines blocks.
    * And when the function is suspend,
      we can't call it directly.
     */