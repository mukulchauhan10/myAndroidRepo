package com.example.roomdbapplication.database

import androidx.room.*

@Entity
data class Task(
    val tName: String?,
    val tTask: String?,
    val tEditDate: String,
    val tRemainderDate: String? = null,
    val tRemainderTime: String? = null,
    val tActivate: Boolean,
    @PrimaryKey(autoGenerate = true)
    val tID: Int = 0
)