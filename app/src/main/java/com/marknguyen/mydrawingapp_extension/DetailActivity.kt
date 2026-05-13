package com.marknguyen.mydrawingapp_extension

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_LOCATION = "extra_location"
        const val EXTRA_POSITION = "extra_position"
    }

    private lateinit var location: Location
    private var position = -1

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCity: TextInputLayout
    private lateinit var tilLastVisit: TextInputLayout
    private lateinit var tilDescription: TextInputLayout
    private lateinit var editName: TextInputEditText
    private lateinit var editCity: TextInputEditText
    private lateinit var editLastVisit: TextInputEditText
    private lateinit var editDescription: TextInputEditText
    private lateinit var ratingBar: RatingBar
    private lateinit var switchVisited: SwitchMaterial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        @Suppress("DEPRECATION")
        location = intent.getParcelableExtra<Location>(EXTRA_LOCATION) ?: run {
            finish()
            return
        }
        position = intent.getIntExtra(EXTRA_POSITION, -1)

        val toolbar = findViewById<Toolbar>(R.id.toolbarDetail)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = if (position < 0) getString(R.string.title_add_location) else location.name

        initViews()
        populateForm()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (validateAndSave()) {
                    isEnabled = false
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun initViews() {
        tilName = findViewById(R.id.tilName)
        tilCity = findViewById(R.id.tilCity)
        tilLastVisit = findViewById(R.id.tilLastVisit)
        tilDescription = findViewById(R.id.tilDescription)
        editName = findViewById(R.id.editTextName)
        editCity = findViewById(R.id.editTextCity)
        editLastVisit = findViewById(R.id.editTextLastVisit)
        editDescription = findViewById(R.id.editTextDescription)
        ratingBar = findViewById(R.id.ratingBarDetail)
        switchVisited = findViewById(R.id.switchVisited)

        editLastVisit.setOnClickListener { showDatePicker() }

        switchVisited.setOnCheckedChangeListener { _, checked ->
            switchVisited.text = getString(
                if (checked) R.string.label_visited_yes else R.string.label_visited_no
            )
        }
    }

    private fun populateForm() {
        findViewById<ImageView>(R.id.imageDetail).apply {
            setImageResource(location.imageResId)
            contentDescription = location.name
        }
        editName.setText(location.name)
        editCity.setText(location.city)
        editLastVisit.setText(location.lastVisit)
        editDescription.setText(location.description)
        ratingBar.rating = location.rating
        switchVisited.isChecked = location.isVisited
        switchVisited.text = getString(
            if (location.isVisited) R.string.label_visited_yes else R.string.label_visited_no
        )
    }

    private fun showDatePicker() {
        val calendar = parseDate(editLastVisit.text.toString()) ?: Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, _ ->
                val formatted = SimpleDateFormat("MMMM yyyy", Locale.getDefault()).format(
                    Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, month)
                        set(Calendar.DAY_OF_MONTH, 1)
                    }.time
                )
                editLastVisit.setText(formatted)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun parseDate(text: String): Calendar? = try {
        SimpleDateFormat("MMMM yyyy", Locale.getDefault()).parse(text)?.let {
            Calendar.getInstance().apply { time = it }
        }
    } catch (e: Exception) {
        null
    }

    private fun validateAndSave(): Boolean {
        val name = editName.text.toString().trim()
        val city = editCity.text.toString().trim()
        val lastVisit = editLastVisit.text.toString().trim()
        val description = editDescription.text.toString().trim()
        var isValid = true

        if (name.length < 2) {
            tilName.error = getString(R.string.error_name_length)
            isValid = false
        } else {
            tilName.error = null
        }

        if (city.isBlank()) {
            tilCity.error = getString(R.string.error_city_required)
            isValid = false
        } else {
            tilCity.error = null
        }

        if (parseDate(lastVisit) == null) {
            tilLastVisit.error = getString(R.string.error_date_invalid)
            isValid = false
        } else {
            tilLastVisit.error = null
        }

        if (description.length < 5) {
            tilDescription.error = getString(R.string.error_description_length)
            isValid = false
        } else {
            tilDescription.error = null
        }

        if (!isValid) return false

        val updated = location.copy(
            name = name,
            city = city,
            lastVisit = lastVisit,
            description = description,
            rating = ratingBar.rating,
            isVisited = switchVisited.isChecked
        )

        when {
            position < 0 -> {
                LocationRepository.add(updated)
                location = updated
                position = LocationRepository.locations.size - 1
                Toast.makeText(this, getString(R.string.toast_added, updated.name), Toast.LENGTH_SHORT).show()
            }
            updated != location -> {
                LocationRepository.update(position, updated)
                location = updated
                Toast.makeText(this, getString(R.string.toast_updated, updated.name), Toast.LENGTH_SHORT).show()
            }
        }

        return true
    }
}
