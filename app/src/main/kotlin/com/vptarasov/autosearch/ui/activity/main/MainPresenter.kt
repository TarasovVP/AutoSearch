package com.vptarasov.autosearch.ui.activity.main

import android.widget.Toast
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.model.User
import io.reactivex.disposables.CompositeDisposable

class MainPresenter: MainContract.Presenter {

    private val subscriptions = CompositeDisposable()
    private lateinit var view: MainContract.View

    override fun subscribe() {

    }

    override fun unsubscribe() {
        subscriptions.clear()
    }

    override fun attach(view: MainContract.View) {
        this.view = view
    }

    override fun checkUserWithFireStore(firebaseUser: FirebaseUser) {
        val doc = FirebaseFirestore.getInstance().collection("user")
            .document(firebaseUser.uid)
        doc
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        App.instance!!.user = document.toObject(User::class.java)!!
                        view.showUserInfo()
                        view.showCarsListFragment()
                    } else {
                        doc.set(firebaseUser)
                        view.showUserActivity()
                    }

                } else {
                    Toast.makeText(App.instance, "Ошибка", Toast.LENGTH_LONG).show()
                }
            }
    }
}