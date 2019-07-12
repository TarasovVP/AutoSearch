package com.vptarasov.autosearch.model

import java.io.Serializable

data class SearchData(var body: Map<String, String>? = null,
                 var color: Map<String, String>? = null,
                 var drive: Map<String, String>? = null,
                 var mark: Map<String, String>? = null,
                 var region: Map<String, String>? = null,
                 var engine: Map<String, String>? = null,
                 var year: Map<String, String>? = null) : Serializable
