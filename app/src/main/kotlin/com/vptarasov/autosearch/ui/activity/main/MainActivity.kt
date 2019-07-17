package com.vptarasov.autosearch.ui.activity.main

import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.di.component.DaggerActivityComponent
import com.vptarasov.autosearch.di.module.ActivityModule
import com.vptarasov.autosearch.model.SearchData
import com.vptarasov.autosearch.ui.fragments.cars_list.CarsListFragment
import com.vptarasov.autosearch.ui.fragments.favourite.FavouriteFragment
import com.vptarasov.autosearch.ui.fragments.news_preview.NewsPreviewFragment
import com.vptarasov.autosearch.ui.fragments.search.SearchFragment
import com.vptarasov.autosearch.util.FragmentUtil
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


open class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    MainContract.View {

    @Inject
    lateinit var presenter: MainContract.Presenter

    private lateinit var searchFragment: Fragment
    private lateinit var carsListFragment: Fragment
    private lateinit var favouriteListFragment: Fragment
    private lateinit var newsPreviewFragment: Fragment
    private lateinit var searchData: SearchData

    override fun showCarsListFragment() {
        FragmentUtil.replaceFragment(supportFragmentManager, carsListFragment, true)
    }

    override fun showSearchFragment() {
        val bundle = Bundle()
        bundle.putSerializable("searchData", searchData)
        searchFragment.arguments = bundle
        FragmentUtil.replaceFragment(supportFragmentManager, searchFragment, true)
    }

    override fun showFavouriteListFragment() {
        FragmentUtil.replaceFragment(supportFragmentManager, favouriteListFragment, true)
    }

    override fun showNewsPreviewFragment() {
        FragmentUtil.replaceFragment(supportFragmentManager, newsPreviewFragment, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.vptarasov.autosearch.R.layout.activity_main)
        injectDependency()

        val intent = intent
        searchData = intent.getSerializableExtra("searchData") as SearchData

        searchFragment = SearchFragment()
        carsListFragment = CarsListFragment()
        favouriteListFragment = FavouriteFragment()
        newsPreviewFragment = NewsPreviewFragment()


        setNavigationVisibiltity(true)
        navigation.setOnNavigationItemSelectedListener(this)

        if (App.instance?.isNetworkAvailable()!!) {
            navigation.visibility = View.VISIBLE
        } else {
            offLineText.visibility = View.VISIBLE
            offLineImage.visibility = View.VISIBLE
        }

        presenter.attach(this)


    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            com.vptarasov.autosearch.R.id.catalogue -> showCarsListFragment()
            com.vptarasov.autosearch.R.id.search_car -> showSearchFragment()
            com.vptarasov.autosearch.R.id.favouriteList -> showFavouriteListFragment()
            com.vptarasov.autosearch.R.id.articles -> showNewsPreviewFragment()
        }
        return true
    }

    private fun setNavigationVisibiltity(b: Boolean) {
        if (b) {
            navigation.visibility = View.VISIBLE
        } else {
            navigation.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count > 0) {
            supportFragmentManager.popBackStack()
        } else {
            exitByBackKey()
        }

    }

    private fun injectDependency() {
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()

        activityComponent.inject(this)
    }


    private fun exitByBackKey() {
        val builder = AlertDialog.Builder(this@MainActivity)
        builder.setMessage(getString(com.vptarasov.autosearch.R.string.want_to_exit_app))
        builder.setCancelable(true)

        builder.setPositiveButton(
            resources.getString(com.vptarasov.autosearch.R.string.Yes)
        ) { _, _ -> finish() }

        builder.setNegativeButton(
            resources.getString(com.vptarasov.autosearch.R.string.No)
        ) { dialog, _ -> dialog.cancel() }

        val alert = builder.create()
        alert.show()


    }




}
