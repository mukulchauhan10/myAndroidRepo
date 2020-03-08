package com.example.roomdbapplication.database

import android.content.Context
import androidx.room.Room

class DataBaseClient(mContext: Context?) {
    val taskDatabase: TaskDatabase

    companion object {
        private var mInstance: DataBaseClient? = null

        @Synchronized
        fun getInstance(mContext: Context?): DataBaseClient? {
            if (mInstance == null) mInstance =
                DataBaseClient(mContext)
            return mInstance
        }
    }

    init {
        taskDatabase =
            Room.databaseBuilder(mContext!!, TaskDatabase::class.java, "MyTask").build()
    }
}