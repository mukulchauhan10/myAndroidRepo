package com.example.notes.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDAO {
    @Insert
    fun insertNote(note: Note)

    @Query("select * from Note")
    fun getAllNote():List<Note>

    @Insert
    fun insertMultipleNotes(vararg note: Note)
}