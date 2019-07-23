package com.vptarasov.autosearch.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class News(var title: String? = null, var text: String? = null, var url: String? = null, var photo: String? = null){

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "title" to title,
            "text" to text,
            "url" to url,
            "photo" to photo
        )
    }
}