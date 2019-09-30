package com.vptarasov.autosearch.ui.activity.main

import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.model.User
import com.vptarasov.autosearch.ui.fragments.BasePresenter

class MainPresenter: BasePresenter<MainContract.View>(), MainContract.Presenter {

    override fun checkUserWithFireStore(firebaseUser: FirebaseUser) {
        val doc = App.instance?.firebaseFirestore?.collection("user")
            ?.document(firebaseUser.uid)
        doc
            ?.get()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        App.instance!!.user = document.toObject(User::class.java)!!
                        getView()?.showUserInfo()
                        getView()?.showCarsListFragment()
                    } else {
                        val newUser = User()
                        newUser.id = firebaseUser.uid
                        doc.set(newUser)
                        App.instance!!.user = newUser
                        getView()?.showUserInfo()
                        getView()?.showUserActivity()
                    }

                } else {
                    Toast.makeText(App.instance, "Ошибка", Toast.LENGTH_LONG).show()
                }
            }
    }
}