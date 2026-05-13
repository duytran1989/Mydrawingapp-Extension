package com.marknguyen.mydrawingapp_extension

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: LocationAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmptyState: TextView
    private val displayedLocations = mutableListOf<Location>()
    private var currentQuery = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))

        recyclerView = findViewById(R.id.rvLocations)
        tvEmptyState = findViewById(R.id.tvEmptyState)

        displayedLocations.addAll(LocationRepository.locations)
        adapter = LocationAdapter(displayedLocations) { displayPos, location ->
            val repoIndex = LocationRepository.locations.indexOf(location)
            startActivity(Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_LOCATION, location)
                putExtra(DetailActivity.EXTRA_POSITION, repoIndex)
            })
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        setupSwipeToDelete()
        setupSearch()

        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            val blank = Location(
                name = "", city = "", lastVisit = "", rating = 0f,
                description = "", imageResId = R.drawable.ic_launcher_background, isVisited = false
            )
            startActivity(Intent(this, DetailActivity::class.java).apply {
                putExtra(DetailActivity.EXTRA_LOCATION, blank)
                putExtra(DetailActivity.EXTRA_POSITION, -1)
            })
        }

        updateEmptyState()
    }

    private fun setupSearch() {
        findViewById<TextInputEditText>(R.id.etSearch).addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                currentQuery = s?.toString().orEmpty()
                applyFilter()
            }
        })
    }

    private fun applyFilter() {
        displayedLocations.clear()
        displayedLocations.addAll(
            if (currentQuery.isBlank()) LocationRepository.locations
            else LocationRepository.locations.filter {
                it.name.contains(currentQuery, ignoreCase = true) ||
                it.city.contains(currentQuery, ignoreCase = true)
            }
        )
        adapter.notifyDataSetChanged()
        updateEmptyState()
    }

    private fun updateEmptyState() {
        val empty = displayedLocations.isEmpty()
        recyclerView.visibility = if (empty) View.GONE else View.VISIBLE
        tvEmptyState.visibility = if (empty) View.VISIBLE else View.GONE
        if (empty) {
            tvEmptyState.text = getString(
                if (LocationRepository.locations.isEmpty()) R.string.empty_no_locations
                else R.string.empty_no_results
            )
        }
    }

    private fun setupSwipeToDelete() {
        val callback = object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(rv: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val displayPos = viewHolder.adapterPosition
                val location = displayedLocations[displayPos]
                val repoIndex = LocationRepository.locations.indexOf(location)

                LocationRepository.remove(repoIndex)
                displayedLocations.removeAt(displayPos)
                adapter.notifyItemRemoved(displayPos)
                updateEmptyState()

                Snackbar.make(recyclerView, "${location.name} deleted", Snackbar.LENGTH_LONG)
                    .setAnchorView(R.id.fabAdd)
                    .setAction("Undo") {
                        LocationRepository.insert(repoIndex, location)
                        applyFilter()
                    }
                    .show()
            }
        }
        ItemTouchHelper(callback).attachToRecyclerView(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        applyFilter()
    }
}
