package com.example.notes.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String,
    val note: String,
    val date: String,
    val time: String,
    @PrimaryKey(autoGenerate = true)
    val noteId: Int = 1
)