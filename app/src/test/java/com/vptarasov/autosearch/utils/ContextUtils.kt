package com.vptarasov.autosearch.utils

import android.app.Application
import android.content.Context
import com.vptarasov.autosearch.R
import org.mockito.Mockito


const val STRING_UNKNOWN_ERROR = "STRING_UNKNOWN_ERROR"

fun getTestContext(): Context {
    val application: Application = Mockito.mock(Application::class.java)
    Mockito.`when`(application.getString(R.string.error)).thenReturn(STRING_UNKNOWN_ERROR)
    Mockito.`when`(application.applicationContext).thenReturn(application)
    return application
}