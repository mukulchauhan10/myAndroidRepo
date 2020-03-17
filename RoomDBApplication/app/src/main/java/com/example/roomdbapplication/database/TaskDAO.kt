package com.example.roomdbapplication.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TaskDAO {

    /*
    * we have to use suspend keyword before fun,
      to execute in coroutines blocks.
    * And when the function is suspend,
      we can't call it directly.
     */

    @Insert
    suspend fun insertTask(task: Task)

    @Query("select * from Task")
    suspend fun getAllTask(): List<Task>

    /*@Delete()
    fun deleteTask(tID: Int)*/
}