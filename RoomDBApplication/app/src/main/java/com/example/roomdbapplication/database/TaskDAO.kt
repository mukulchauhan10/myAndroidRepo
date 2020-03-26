package com.example.roomdbapplication.database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.text.FieldPosition

@Dao
interface TaskDAO {
    @Insert
    suspend fun insertTask(task: Task)

    @Query("select * from Task order by tid desc")
    fun getAllTask(): LiveData<List<Task>>

    @Query("delete from Task where tid = :uid")
    suspend fun deleteTask(uid: Long)

    @Query("update Task set tID=:toPosition where tID=:fromPosition")
    suspend fun changeTaskPosition(fromPosition: Int, toPosition: Int)

    @Query(
        "update Task set tName = :uTitle, tTask = :uTask, tEditDate = :uEditDate, tRemainderDate = :uRemDate, tRemainderTime = :uRemTime, tActivate = :uActivation where tID = :uId"
    )
    suspend fun updateTask(
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