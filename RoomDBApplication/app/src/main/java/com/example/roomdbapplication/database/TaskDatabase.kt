package com.example.roomdbapplication.database

import android.content.Context
import androidx.room.*
import com.example.roomdbapplication.Activity.DB_NAME

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun getDao(): TaskDAO

    companion object {
        @Volatile
        private var mInstance: TaskDatabase? = null
        /*operator fun invoke(context: Context) = mInstance ?: synchronized(this) {
            mInstance ?: buildDatabase(context)
        }

        fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            TaskDatabase::class.java,
            DB_NAME
        ).fallbackToDestructiveMigration().build()*/


        fun buildDatabase(context: Context): TaskDatabase {
            val tempInstance = mInstance
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    DB_NAME
                ).fallbackToDestructiveMigration().build()
                mInstance = instance
                return instance
            }
        }
    }
}
