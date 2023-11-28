package com.example.room2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

// Kelas abstrak yang mewakili Room Database dan menyediakan instance dari NoteDao
@Database(entities = [Note::class], version = 1, exportSchema = false)
abstract class NoteRoomDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao?

    companion object {
        @Volatile
        private var INSTANCE: NoteRoomDatabase? = null

        // Metode untuk mendapatkan instance dari NoteRoomDatabase
        fun getDatabase(context: Context): NoteRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(NoteRoomDatabase::class.java) {
                    INSTANCE = databaseBuilder(
                        context.applicationContext,
                        NoteRoomDatabase::class.java, "note_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}