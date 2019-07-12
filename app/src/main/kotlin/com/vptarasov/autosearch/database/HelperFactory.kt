package com.vptarasov.autosearch.database

import android.content.Context
import com.j256.ormlite.android.apptools.OpenHelperManager

object HelperFactory {

    var helper: DatabaseHelper? = null
        private set

    fun setHelper(context: Context) {
        helper = OpenHelperManager.getHelper(context, DatabaseHelper::class.java)
    }

    fun releaseHelper() {
        OpenHelperManager.releaseHelper()
        helper = null
    }
}