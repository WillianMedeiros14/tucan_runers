package com.example.runners.model

import android.os.Parcelable
import com.example.runners.ui.adapter.FirebaseHelper
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task(
    var id: String = "",
    var description: String = "",
   var status: Int = 0
) : Parcelable {
    init {
        this.id = FirebaseHelper.getDatabase().push().key ?:""
    }
}
