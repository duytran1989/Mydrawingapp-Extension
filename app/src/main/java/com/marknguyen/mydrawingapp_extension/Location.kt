package com.marknguyen.mydrawingapp_extension
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val name: String,
    val city: String,
    val lastVisit: String,
    val rating: Float,
    val description: String,
    val imageResId: Int,
    val isVisited: Boolean
) : Parcelable

