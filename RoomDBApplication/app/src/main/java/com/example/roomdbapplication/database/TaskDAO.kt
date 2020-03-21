package com.example.roomdbapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDAO {
    @Insert
    suspend fun insertTask(task: Task)

    @Query("select * from Task order by tid desc")
    fun getAllTask(): LiveData<List<Task>>

    @Query("select * from Task where tID = :taskID")
    suspend fun getTheTask(taskID: Int)

    @Query("delete from Task where tid = :uid")
    fun deleteTask(uid: Int)

    @Query("update Task set tName = :uTitle, tTask = :uTask, tEditDate = :uEditDate, tRemainderDate = :uRemDate, tRemainderTime = :uRemTime, tActivate = :uActivation where tID = :uId")
    fun updateTask(
        uTitle: String?,
        uTask: String?,
        uEditDate: String,
        uRemDate: String?,
        uRemTime: String?,
        uActivation: Boolean,
        uId: Int
    )
    /*@Delete()
    fun deleteTask(tID: Int)*/
}

/*
    * we have to use suspend keyword before fun,
      to execute in coroutines blocks.
    * And when the function is suspend,
      we can't call it directly.
     */