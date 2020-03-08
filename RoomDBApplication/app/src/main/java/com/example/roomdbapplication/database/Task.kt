package com.example.roomdbapplication.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Task(
    val tName: String,
    val tDescription: String,
    val tCreationDate: Date,
    val tAchieveDate: Date,
    val tComment: String,
    val tStatus: Boolean,
    val tActivate: Boolean,
    @PrimaryKey(autoGenerate = true)
    val tID: Int =1
)