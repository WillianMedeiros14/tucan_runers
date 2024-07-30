package com.example.runners.model

import android.os.Parcelable
import com.example.runners.ui.adapter.FirebaseHelper
import kotlinx.parcelize.Parcelize


data class User(
    var uid: String = "",
    var email: String = "",
    var name: String = "",
    var gender: String = "",
    var editDateOfBirth: String = ""
)
