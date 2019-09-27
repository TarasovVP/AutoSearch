package com.vptarasov.autosearch.ui.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.model.Car

abstract class BaseCarFragment<Self: BaseCarFragment<Self, T>, T: BaseCarFragment<T, Self>>: Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }


    fun addDeleCarWithFirebase(car: Car) {
        val doc = App.instance?.firebaseFirestore?.collection("user")
            ?.document(App.instance!!.user.id)?.collection(("cars"))?.document(car.urlToId())
        doc
            ?.get()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        doc.delete()
                        car.setBookmarked(false)
                    } else {
                        car.setBookmarked(true)
                        doc.set(car)
                    }
                } else {
                    Toast.makeText(App.instance, "Ошибка", Toast.LENGTH_LONG).show()
                }
                Toast.makeText(
                    context, App.instance?.getString(
                        if (car.isBookmarked())
                            R.string.add_favor
                        else
                            R.string.delete_favor
                    ), Toast.LENGTH_SHORT
                ).show()
            }
    }

    abstract fun injectDependency()

}