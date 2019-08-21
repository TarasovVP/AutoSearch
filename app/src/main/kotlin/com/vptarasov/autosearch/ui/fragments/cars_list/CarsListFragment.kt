package com.vptarasov.autosearch.ui.fragments.cars_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.di.component.DaggerFragmentComponent
import com.vptarasov.autosearch.di.module.FragmentModule
import com.vptarasov.autosearch.model.Car
import com.vptarasov.autosearch.model.QueryDetails
import com.vptarasov.autosearch.model.User
import com.vptarasov.autosearch.ui.fragments.base.BaseFragment
import com.vptarasov.autosearch.ui.fragments.car.CarFragment
import com.vptarasov.autosearch.util.FragmentUtil
import kotlinx.android.synthetic.main.fragment_cars_list.view.*
import kotlinx.android.synthetic.main.list_pages.view.*
import javax.inject.Inject

class CarsListFragment : BaseFragment(), CarsListContract.View {

    private var adapter: CarsListAdapter? = null
    private var page: Int = 1
    private var lastPage: Int = 0
    private var queryDetails: QueryDetails? = null
    private var cars: ArrayList<Car>? = null
    private lateinit var recyclerViewCar: RecyclerView
    private lateinit var btnRight: Button
    private lateinit var btnLeft: Button
    private lateinit var tvPage: TextView
    private lateinit var searchPaging: RelativeLayout
    private lateinit var progressBar: ProgressBar

    @Inject
    lateinit var presenter: CarsListContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        injectDependency()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflateWithLoadingIndicator(R.layout.fragment_cars_list, container)
        initView(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val args = arguments
        if (cars == null) {
            if (args?.getSerializable("queryDetails") != null) {
                queryDetails = args.getSerializable("queryDetails") as QueryDetails
                presenter.loadCars(queryDetails, page)

            } else {
                queryDetails = QueryDetails()
                presenter.loadCars(queryDetails, page)
            }
        } else {
            initAdapter(cars!!, lastPage)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attach(this)
        presenter.subscribe()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unsubscribe()
    }

    override fun getLastPage(lastPage: Int) {
        this.lastPage = lastPage
    }

    @SuppressLint("SetTextI18n")
    private fun preformSearch() {
        presenter.loadCars(queryDetails, page)
        tvPage.text = App.instance?.getString(R.string.page) + " " + page + " из " + lastPage
        recyclerViewCar.smoothScrollToPosition(0)
    }

    override fun initView(view: View) {
        recyclerViewCar = view.recyclerViewCar
        btnRight = view.btnRight
        btnLeft = view.btnLeft
        tvPage = view.tvPage
        searchPaging = view.searchPaging as RelativeLayout
        progressBar = view.progressBarCarsList

        btnRight.setOnClickListener {
            if (page < lastPage) {
                page++
                preformSearch()
            }
        }
        btnLeft.setOnClickListener {
            if (page > 1) {
                page--
                preformSearch()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initAdapter(cars: ArrayList<Car>, lastPage: Int) {
        adapter = CarsListAdapter(cars)
        adapter?.setListener(this)
        recyclerViewCar.layoutManager = LinearLayoutManager(context)
        recyclerViewCar.adapter = adapter
        tvPage.text = App.instance?.getString(R.string.page) + " " + page + " из " + lastPage
        searchPaging.visibility = View.GONE
    }

    override fun onLastItemReached() {
        if (searchPaging.visibility != View.VISIBLE) {
            searchPaging.visibility = View.VISIBLE
        }
    }

    override fun onLastItemLeft() {
        if (searchPaging.visibility != View.GONE) {
            searchPaging.visibility = View.GONE
        }
    }

    override fun onItemClick(car: Car) {
        val bundle = Bundle()
        bundle.putString("carUrl", car.url)
        val carFragment = CarFragment()
        carFragment.arguments = bundle
        fragmentManager?.let { FragmentUtil.replaceFragment(it, carFragment, true) }

    }

 /*   override fun onFavoriteClick(car: Car) {
        val doc = FirebaseFirestore.getInstance().collection("car")
            .document(car.urlToId() + FirebaseAuth.getInstance().currentUser?.uid)

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
                adapter?.updateFavIcon(car)

            }
    }*/

    override fun onFavoriteClick(car: Car) {
        val doc = FirebaseFirestore.getInstance().collection("user")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)

        doc
            .get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val document = task.result
                    if (document!!.exists()) {
                        doc.collection("cars").document(car.urlToId())

                    }
                } else {
                    Toast.makeText(App.instance, "Ошибка", Toast.LENGTH_LONG).show()
                }
            }
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

    private fun setCarToUser(car: Car, user: User) {
        val doc = FirebaseFirestore.getInstance().collection("user").document(user.id)


        doc
            .get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val document = task.result
                    doc.set(car)


                    /*val document = task.result
                    if (document!!.exists()) {
                        doc.delete()
                        car.setBookmarked(false)
                    } else {
                        car.setBookmarked(true)
                        doc.set(car)
                    }*/
                }
            }
    }
}
