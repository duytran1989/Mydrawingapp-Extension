package com.marknguyen.mydrawingapp_extension

object LocationRepository {

    private val _locations: MutableList<Location> = mutableListOf(
        Location(
            name = "Great Ocean Road",
            city = "Victoria, Australia",
            lastVisit = "January 2024",
            rating = 5.0f,
            description = "Scenic coastal drive with stunning ocean views.",
            imageResId = R.drawable.greatoceanroad,
            isVisited = true
        ),
        Location(
            name = "Yarra Valley",
            city = "Victoria, Australia",
            lastVisit = "March 2023",
            rating = 4.5f,
            description = "Beautiful wine region with lush green hills.",
            imageResId = R.drawable.yarra_valley,
            isVisited = true
        ),
        Location(
            name = "Dandenong Ranges",
            city = "Victoria, Australia",
            lastVisit = "June 2023",
            rating = 4.0f,
            description = "Tranquil mountain forests and scenic villages.",
            imageResId = R.drawable.dandenong_ranges,
            isVisited = false
        ),
        Location(
            name = "Phillip Island",
            city = "Victoria, Australia",
            lastVisit = "December 2023",
            rating = 4.5f,
            description = "Home of the famous Penguin Parade.",
            imageResId = R.drawable.philip_island,
            isVisited = true
        )
    )

    val locations: List<Location> get() = _locations

    fun update(index: Int, updated: Location) {
        if (index in _locations.indices) {
            _locations[index] = updated
        }
    }

    fun remove(index: Int): Location? =
        if (index in _locations.indices) _locations.removeAt(index) else null

    fun insert(index: Int, location: Location) {
        _locations.add(index.coerceIn(0, _locations.size), location)
    }
}
