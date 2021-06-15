package hu.bme.aut.nihongo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.size
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import hu.bme.aut.nihongo.adapter.KanjiAdapter
import hu.bme.aut.nihongo.data.KanjiItem
import hu.bme.aut.nihongo.data.KanjiListDatabase
import hu.bme.aut.nihongo.fragments.NewKanjiItemDialogFragment
import kotlinx.android.synthetic.main.kanji_list_activity.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.concurrent.thread

class KanjiListActivity : AppCompatActivity(), KanjiAdapter.KanjiClickListener,
    NewKanjiItemDialogFragment.NewKanjiItemDialogListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: KanjiAdapter
    private lateinit var database: KanjiListDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kanji_list_activity)
        database = Room.databaseBuilder(
            applicationContext,
            KanjiListDatabase::class.java,
            "kanji-list"
        ).build()

        fab.setOnClickListener{
            NewKanjiItemDialogFragment().show(
                supportFragmentManager,
                NewKanjiItemDialogFragment.TAG
            )
        }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerView = MainRecyclerView
        adapter = KanjiAdapter(this)
        loadItemsInBackground()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.kanjiItemDao().getAll()
            runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onItemChanged(item: KanjiItem) {
        thread {
            database.kanjiItemDao().update(item)
            Log.d("KanjiListActivity", "KanjiItem update was successful")
        }
    }

    override fun onItemDeleted(item: KanjiItem) {
        thread {
            database.kanjiItemDao().deleteItem(item)
            runOnUiThread {
                initRecyclerView()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onKanjiItemCreated(newItem: KanjiItem) {
        thread {
            val newId = database.kanjiItemDao().insert(newItem)
            val newShoppingItem = newItem.copy(
                id = newId
            )
            runOnUiThread {
                adapter.addItem(newShoppingItem)
            }
        }
    }
}