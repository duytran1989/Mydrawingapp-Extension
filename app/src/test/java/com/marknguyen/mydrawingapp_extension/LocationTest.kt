package com.marknguyen.mydrawingapp_extension

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LocationTest {

    private val sampleLocation = Location(
        name = "Great Ocean Road",
        city = "Victoria, Australia",
        lastVisit = "January 2024",
        rating = 5.0f,
        description = "Scenic coastal drive.",
        imageResId = 0,
        isVisited = true
    )

    @Test
    fun locationStoresNameCorrectly() {
        assertEquals("Great Ocean Road", sampleLocation.name)
    }

    @Test
    fun locationStoresCityCorrectly() {
        assertEquals("Victoria, Australia", sampleLocation.city)
    }

    @Test
    fun locationStoresRatingCorrectly() {
        assertEquals(5.0f, sampleLocation.rating, 0.01f)
    }

    @Test
    fun locationIsVisitedDefaultsCorrectly() {
        assertTrue(sampleLocation.isVisited)

        val unvisited = sampleLocation.copy(isVisited = false)
        assertFalse(unvisited.isVisited)
    }

    @Test
    fun ratingIsWithinValidRange() {
        val rating = sampleLocation.rating
        assertTrue("Rating must be >= 0", rating >= 0f)
        assertTrue("Rating must be <= 5", rating <= 5f)
    }

    @Test
    fun dataClassEqualityWorksCorrectly() {
        val copy = sampleLocation.copy()
        assertEquals(sampleLocation, copy)
    }

    @Test
    fun copyProducesDistinctObjectWithModifiedField() {
        val modified = sampleLocation.copy(name = "Yarra Valley")
        assertNotEquals(sampleLocation, modified)
        assertEquals("Yarra Valley", modified.name)
        assertEquals(sampleLocation.city, modified.city)
    }

    @Test
    fun locationNameIsNotBlank() {
        assertTrue(sampleLocation.name.isNotBlank())
    }

    @Test
    fun lastVisitStringIsNotEmpty() {
        assertTrue(sampleLocation.lastVisit.isNotEmpty())
    }
}
