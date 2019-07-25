package com.vptarasov.autosearch.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class News(var id: String? = null, var title: String? = null, var text: String? = null, var fullText: String? = null, var url: String? = null, var photo: String? = null)