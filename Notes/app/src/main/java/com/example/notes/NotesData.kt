package com.example.notes

data class NotesData(
    val title: String,
    val note: String,
    val date: String,
    val time: String
)

object Application {
    val notesList = arrayListOf<NotesData>()
}

