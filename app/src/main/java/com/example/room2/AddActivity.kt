package com.example.room2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.room2.database.Note
import com.example.room2.database.NoteDao
import com.example.room2.database.NoteRoomDatabase
import com.example.room2.databinding.ActivityAddBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AddActivity : AppCompatActivity() {
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService
    private lateinit var binding: ActivityAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menggunakan View Binding untuk mengakses tampilan layut activity_add
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ExecutorService untuk menjalankan operasi database di thread terpisah
        executorService = Executors.newSingleThreadExecutor()

        // Mendapatkan instance dari NoteDao dari NoteRoomDatabase
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        // Menambahkan listener onClick ke tombol btnAdd
        binding.btnAdd.setOnClickListener {
            // Memanggil fungsi insert() untuk menambahkan catatan ke database
            insert(
                Note(
                    title = binding.editTitle.text.toString(),
                    description = binding.editDesc.text.toString(),
                    date = binding.editDate.text.toString()
                )
            )
            // Menutup activity setelah menambahkan catatan
            finish()
        }
    }

    // Fungsi untuk menambahkan catatan ke database menggunakan ExecutorService
    private fun insert(note: Note) {
        // Menjalankan operasi insert di thread terpisah
        executorService.execute { mNotesDao.insert(note) }
    }
}
