package com.example.roomdbapplication.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {


    abstract fun getDao(): TaskDAO

    companion object {
        @Volatile
        private var mInstance: TaskDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = mInstance ?: synchronized(LOCK) {
            mInstance ?: buildDatabase(context)
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java,
                "noteDatabase1"
            ).build()


    }
}
