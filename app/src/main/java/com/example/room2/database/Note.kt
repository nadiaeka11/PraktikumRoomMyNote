package com.example.room2.database

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Mendefinisikan entitas Note untuk digunakan oleh Room Database
@Entity(tableName = "note_table")
data class Note(
    // ID merupakan primary key yang akan di-generate secara otomatis
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int=0,

    // Kolom title untuk menyimpan judul catatan
    @ColumnInfo(name = "title")
    val title: String,

    // Kolom description untuk menyimpan deskripsi catatan
    @ColumnInfo(name = "description")
    val description: String,

    // Kolom date untuk menyimpan tanggal catatan
    @ColumnInfo(name = "date")
    val date: String
    )