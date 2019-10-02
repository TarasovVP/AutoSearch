package com.vptarasov.autosearch.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.tapadoo.alerter.Alerter
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.model.Car
import java.util.*

abstract class BaseCarFragment<Self : BaseCarFragment<Self, T>, T : BaseCarFragment<T, Self>> :
    Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView(view)
        showProgress()
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

    fun showErrorMessage(error: String) {
        Alerter.create(Objects.requireNonNull<FragmentActivity>(activity))
            .setIconColorFilter(ContextCompat.getColor(activity!!, android.R.color.white))
            .setBackgroundColorRes(R.color.colorError)
            .setIcon(R.drawable.ic_close_circle)
            .setTitle(R.string.error)
            .setText(error)
            .show()
    }

    abstract fun injectDependency()
    abstract fun initView(view: View)
    abstract fun showProgress()


}