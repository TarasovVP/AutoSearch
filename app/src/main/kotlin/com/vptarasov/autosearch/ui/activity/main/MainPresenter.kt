package com.vptarasov.autosearch.ui.activity.main

import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.model.User

class MainPresenter: MainContract.Presenter {

    private lateinit var view: MainContract.View

    override fun attach(view: MainContract.View) {
        this.view = view
    }

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
                        view.showUserInfo()
                        view.showCarsListFragment()
                    } else {
                        val newUser = User()
                        newUser.id = firebaseUser.uid
                        doc.set(newUser)
                        App.instance!!.user = newUser
                        view.showUserInfo()
                        view.showUserActivity()
                    }

                } else {
                    Toast.makeText(App.instance, "Ошибка", Toast.LENGTH_LONG).show()
                }
            }
    }
}