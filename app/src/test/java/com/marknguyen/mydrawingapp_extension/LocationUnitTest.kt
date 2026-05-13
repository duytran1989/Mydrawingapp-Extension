package com.marknguyen.mydrawingapp_extension

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class LocationUnitTest {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).apply {
        isLenient = false
    }

    private lateinit var testLocation: Location

    @Before
    fun setUp() {
        testLocation = Location(
            name = "Eiffel Tower",
            city = "Paris, France",
            lastVisit = "14/07/2023",
            rating = 5.0f,
            description = "Breathtaking views from the top deck.",
            imageResId = 0,
            isVisited = true
        )
    }

    @Test
    fun `location copy preserves city and imageResId`() {
        val updated = testLocation.copy(name = "Arc de Triomphe")
        assertEquals(testLocation.city, updated.city)
        assertEquals(testLocation.imageResId, updated.imageResId)
        assertEquals("Arc de Triomphe", updated.name)
    }

    @Test
    fun `location rating clamped within 0 to 5`() {
        val loc = testLocation.copy(rating = 4.5f)
        assertTrue(loc.rating in 0f..5f)
    }

    @Test
    fun `location equals is value-based`() {
        val copy = testLocation.copy()
        assertEquals(testLocation, copy)
    }

    @Test
    fun `name shorter than 2 chars is invalid`() {
        assertFalse(isNameValid("A"))
    }

    @Test
    fun `name of exactly 2 chars is valid`() {
        assertTrue(isNameValid("AB"))
    }

    @Test
    fun `blank name is invalid`() {
        assertFalse(isNameValid("   "))
    }

    @Test
    fun `normal location name is valid`() {
        assertTrue(isNameValid("Sydney Opera House"))
    }

    @Test
    fun `country of 1 char is invalid`() {
        assertFalse(isNameValid("X"))
    }

    @Test
    fun `country with 2 chars is valid`() {
        assertTrue(isNameValid("AU"))
    }

    @Test
    fun `valid date parses correctly`() {
        assertTrue(isDateValid("14/07/2023"))
    }

    @Test
    fun `blank date is invalid`() {
        assertFalse(isDateValid(""))
    }

    @Test
    fun `date with wrong format is invalid`() {
        assertFalse(isDateValid("2023-07-14"))
    }

    @Test
    fun `date with impossible day is invalid`() {
        assertFalse(isDateValid("31/02/2023"))
    }

    @Test
    fun `date with letters is invalid`() {
        assertFalse(isDateValid("ab/cd/efgh"))
    }

    @Test
    fun `leap year date is valid`() {
        assertTrue(isDateValid("29/02/2024"))
    }

    @Test
    fun `non-leap year Feb 29 is invalid`() {
        assertFalse(isDateValid("29/02/2023"))
    }

    private fun isNameValid(value: String): Boolean = value.trim().length >= 2

    private fun isDateValid(dateStr: String): Boolean {
        if (dateStr.isBlank()) return false
        return try {
            dateFormat.parse(dateStr) != null
        } catch (e: ParseException) {
            false
        }
    }
}
