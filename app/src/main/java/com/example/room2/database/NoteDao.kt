package com.example.room2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

// Data Access Object (DAO) untuk operasi-operasi database terkait Note
@Dao
interface NoteDao {
    // Menyisipkan catatan ke dalam database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert (note: Note)

    // Memperbarui catatan dalam database
    @Update
    fun update (note: Note)

    // Menghapus catatan dari database
    @Delete
    fun delete (note: Note)

    // Mengambil semua catatan dari database secara live (menggunakan LiveData)
    @get:Query("SELECT * from note_table ORDER BY id ASC")
    val allNotes: LiveData<List<Note>>
}