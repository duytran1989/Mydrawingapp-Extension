package com.marknguyen.mydrawingapp_extension

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: LocationAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.rvLocations)
        adapter = LocationAdapter(LocationRepository.locations) { position, location ->
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_LOCATION, location)
                putExtra(DetailActivity.EXTRA_POSITION, position)
            }
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        setupSwipeToDelete()
    }

    private fun setupSwipeToDelete() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val removed = LocationRepository.remove(position) ?: return
                adapter.notifyItemRemoved(position)

                Snackbar.make(recyclerView, "${removed.name} deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        LocationRepository.insert(position, removed)
                        adapter.notifyItemInserted(position)
                        recyclerView.scrollToPosition(position)
                    }
                    .show()
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
