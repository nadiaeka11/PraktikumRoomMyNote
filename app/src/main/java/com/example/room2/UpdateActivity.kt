package com.example.room2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.room2.database.Note
import com.example.room2.database.NoteDao
import com.example.room2.database.NoteRoomDatabase
import com.example.room2.databinding.ActivityUpdateBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateBinding
    private var id: Int=0
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        // Menggunakan View Binding untuk mengakses tampilan layut activity_update
        setContentView(binding.root)

        // Mendapatkan data catatan yang dikirimkan dari HomeActivity melalui intent
        val bundle: Bundle? = intent.extras
        id = bundle!!.getInt("EXT_ID")!!
        val title = bundle!!.getString("EXT_TITLE")!!
        val description = bundle!!.getString("EXT_DESCRIPTION")!!
        val date = bundle!!.getString("EXT_DATE")!!

        // Inisialisasi ExecutorService untuk menjalankan operasi database di thread terpisah
        executorService = Executors.newSingleThreadExecutor()

        // Mendapatkan instance dari NoteDao dari NoteRoomDatabase
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        // Mengatur nilai awal pada elemen UI menggunakan data notes yang diterima
        with(binding) {
            editTitle.setText(title)
            editDesc.setText(description)
            editDate.setText(date)

            // Menambahkan listener onClick ke tombol Update untuk mengupdate catatan
            btnUpdate.setOnClickListener {
                update(
                    Note(
                        id = id,
                        title = editTitle.text.toString(),
                        description = editDesc.text.toString(),
                        date = editDate.text.toString()
                    )
                )
                id = 0
                finish()
            }

            // Menambahkan listener onClick ke button Delete untuk menghapus catatan
            btnDelete.setOnClickListener {
                delete(
                    Note(
                        id = id,
                        title = title,
                        description = description,
                        date = date
                    )
                )
                id = 0
                finish()
            }
        }
    }

    // Fungsi untuk melakukan update pada database menggunakan ExecutorService
    private fun update(note: Note) {
        executorService.execute { mNotesDao.update(note) }
    }

    // Fungsi untuk melakukan delete pada database menggunakan ExecutorService
    private fun delete(note: Note) {
        executorService.execute { mNotesDao.delete(note) }
    }
}