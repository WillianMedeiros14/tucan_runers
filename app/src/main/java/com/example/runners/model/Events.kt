package com.example.runners.model

import android.os.Parcelable
import com.example.runners.ui.adapter.FirebaseHelper
import kotlinx.parcelize.Parcelize

@Parcelize
data class Events(
    var id: String = "",
    var date: String = "",
    var location: String = "",
    var name: String = "",
    var image: String = "",
    var distance: List<String> = emptyList(),
    var registrations: String = "",
    var about: String="",
    var address: String="",
    var kit: String="",
    var regulation: String="",
    var route: String="",
) : Parcelable {
    init {
        this.id = FirebaseHelper.getDatabase().push().key ?: ""
    }
}



data class EventResult(
    var id: String = "",
    var date: String = "",
    var location: String = "",
    var name: String = "",
    var image: String = "",
    var distance: List<String> = emptyList(),
    var registration: String = "",
    var about: String="",
    var address: String="",
    var kit: String="",
    var regulation: String="",
    var route: String="",
)
