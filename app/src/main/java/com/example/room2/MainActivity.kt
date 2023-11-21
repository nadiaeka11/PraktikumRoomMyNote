package com.example.room2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.room2.database.Note
import com.example.room2.database.NoteDao
import com.example.room2.database.NoteRoomDatabase
import com.example.room2.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService
    private var updateId: Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        with(binding) {
            btnAdd.setOnClickListener(View.OnClickListener {
                insert(
                    Note(
                        title = editTitle.text.toString(),
                        description = editDesc.text.toString(),
                        date = editDate.text.toString()
                    )
                )
                setEmptyField()
            })

            btnUpdate.setOnClickListener {
                update(
                    Note(
                        id = updateId,
                        title = editTitle.text.toString(),
                        description = editDesc.text.toString(),
                        date = editDate.text.toString()
                    )
                )
                updateId = 0
                setEmptyField()
            }

            listView.setOnItemClickListener { adapterView, _, i, _ ->
                val item = adapterView.adapter.getItem(i) as Note
                updateId = item.id
                editTitle.setText(item.title)
                editDesc.setText(item.description)
                editDate.setText(item.date)
            }

            listView.setOnItemLongClickListener(object : AdapterView.OnItemLongClickListener {
                override fun onItemLongClick(adapterView: AdapterView<*>?, view: View?, i: Int, l: Long): Boolean {
                    val item = adapterView?.adapter?.getItem(i) as Note
                    delete(item)
                    return true
                }
            })
        }
    }

        override fun onResume() {
        super.onResume()
        getAllNotes()
    }

    private fun setEmptyField() {
        with(binding){
            editTitle.setText("")
            editDesc.setText("")
            editDate.setText("")
        }
    }

    private fun getAllNotes() {
        mNotesDao.allNotes.observe(this) { notes ->
            val adapter: ArrayAdapter<Note> = ArrayAdapter<Note>(
                this,
                android.R.layout.simple_list_item_1, notes
            )
            binding.listView.adapter = adapter
        }
    }

    private fun insert(note: Note) {
        executorService.execute { mNotesDao.insert(note) }
    }

    private fun delete(note: Note) {
        executorService.execute { mNotesDao.delete(note) }
    }

    private fun update(note: Note) {
        executorService.execute { mNotesDao.update(note) }
    }

}