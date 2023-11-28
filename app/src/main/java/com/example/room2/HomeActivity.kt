package com.example.room2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.room2.database.Note
import com.example.room2.database.NoteDao
import com.example.room2.database.NoteRoomDatabase
import com.example.room2.databinding.ActivityHomeBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Menggunakan View Binding untuk mengakses tampilan layut activity_home
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ExecutorService untuk menjalankan operasi database di thread terpisah
        executorService = Executors.newSingleThreadExecutor()

        // Mendapatkan instance dari NoteDao dari NoteRoomDatabase
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        // Menambahkan listener onClick ke FAB (Floating Action Button) untuk membuka AddActivity
        binding.fabAddNote.setOnClickListener {
            val intent = Intent(this@HomeActivity, AddActivity::class.java)
            startActivity(intent)
        }

        // Menambahkan listener onItemClick ke ListView untuk membuka UpdateActivity
        binding.listView.setOnItemClickListener { adapterView, view, i, id ->
            // Mendapatkan item yang dipilih dari ListView
            val item = adapterView.adapter.getItem(i) as Note
            // Membuat intent untuk membuka UpdateActivity dan mengirim data catatan
            val intent = Intent(this@HomeActivity, UpdateActivity::class.java)
            intent.putExtra("EXT_ID", item.id)
            intent.putExtra("EXT_TITLE", item.title)
            intent.putExtra("EXT_DESCRIPTION", item.description)
            intent.putExtra("EXT_DATE", item.date)
            startActivity(intent)
        }
    }

    // Override onResume untuk memperbarui tampilan setiap kali aktivitas di-resume
    override fun onResume() {
        super.onResume()
        // Memanggil fungsi getAllNotes() untuk mendapatkan dan menampilkan semua catatan
        getAllNotes()
    }

    // Fungsi untuk mendapatkan semua catatan dari database dan menampilkannya di ListView
    private fun getAllNotes() {
        // Mengamati perubahan pada data catatan dan memperbarui ListView
        mNotesDao.allNotes.observe(this) { notes ->
            // Membuat adapter ArrayAdapter untuk ListView
            val adapter:  ArrayAdapter<Note> = ArrayAdapter<Note>(this,
                android.R.layout.simple_list_item_1, notes)
            // Mengatur adapter ke ListView
            binding.listView.adapter = adapter
        }
    }
}
