package com.vptarasov.autosearch.ui.fragments.favourite

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.di.component.DaggerFragmentComponent
import com.vptarasov.autosearch.di.module.FragmentModule
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.ui.fragments.car.CarFragment
import com.vptarasov.autosearch.util.FragmentUtil
import kotlinx.android.synthetic.main.fragment_favourite_list.view.*
import javax.inject.Inject


class FavouriteFragment : Fragment(), FavouriteContract.View {


    private var adapter: FavouriteAdapter? = null
    private var recyclerView: RecyclerView? = null
    private var noFoundText: TextView? = null
    private lateinit var progressBar: ProgressBar

    @Inject
    lateinit var presenter: FavouriteContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependency()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favourite_list, container, false)
        recyclerView = view.recyclerViewFavourite
        noFoundText = view.noFoundText
        progressBar = view.progressBarFavourite
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
        presenter.loadFavouriteCars()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unsubscribe()
    }

    @SuppressLint("SetTextI18n")
    override fun initAdapter(cars: ArrayList<Car>) {
        if (cars.isNullOrEmpty()) {
            noFoundText?.visibility = View.VISIBLE
        } else {
            adapter = FavouriteAdapter(cars)
            adapter?.setListener(this)
            recyclerView?.layoutManager = LinearLayoutManager(context)
            recyclerView?.adapter = adapter
        }
    }


    override fun onItemClick(car: Car) {
        val bundle = Bundle()
        bundle.putString("carUrl", car.url)
        val carFragment = CarFragment()
        carFragment.arguments = bundle
        fragmentManager?.let { FragmentUtil.replaceFragment(it, carFragment, true) }
    }

    override fun onFavoriteClick(car: Car) {
        val doc = FirebaseFirestore.getInstance().collection("user")
            .document(App.instance!!.user.id).collection(("cars")).document(car.urlToId())

        doc
        .get()
            .addOnCompleteListener { task ->

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

            val carsFromAdapter = adapter!!.cars
            for (i in carsFromAdapter.indices) {
                if (carsFromAdapter[i] == car) {
                    adapter?.notifyItemRemoved(i)
                    adapter?.notifyItemRangeChanged(i, adapter!!.itemCount)
                    adapter?.cars?.removeAt(i)
                    break
                }
            }
        }

    }

    override fun onLastFavoriteRemoved() {
        noFoundText?.visibility = View.VISIBLE
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    private fun injectDependency() {
        val fragmentComponent = DaggerFragmentComponent.builder()
            .fragmentModule(FragmentModule())
            .build()

        fragmentComponent.inject(this)
    }

}
