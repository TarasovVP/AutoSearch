package com.vptarasov.autosearch.ui.activity.user

import android.content.Context
import android.net.Uri
import com.vptarasov.autosearch.model.User
import com.vptarasov.autosearch.ui.AppContract

class UserContract {

    interface View : AppContract.View {
        fun initView()
        fun showMainActivity()
        fun showProgress()
        fun hideProgress()
        fun getNameFromEditText(): String
        fun getEmailFromEditText(): String

    }

    interface Presenter : AppContract.Presenter<View>{
        fun launchGallery()
        fun uploadImage(filePath: Uri?, context: Context)
        fun upDateUser(loggedInUser: User, context: Context)
    }
}