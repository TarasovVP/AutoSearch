package com.vptarasov.autosearch.ui.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.api.Api
import com.vptarasov.autosearch.model.SearchData
import com.vptarasov.autosearch.parser.HTMLParser
import com.vptarasov.autosearch.ui.fragments.CarsListFragment
import com.vptarasov.autosearch.ui.fragments.FavouriteListFragment
import com.vptarasov.autosearch.ui.fragments.NewsPreviewFragment
import com.vptarasov.autosearch.ui.fragments.SearchFragment
import com.vptarasov.autosearch.util.FragmentUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*


open class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var searchFragment: Fragment
    private lateinit var carsListFragment: Fragment
    private lateinit var favouriteListFragment: Fragment
    private lateinit var newsPreviewFragment: Fragment
    private lateinit var searchData: SearchData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.vptarasov.autosearch.R.layout.activity_main)

        loadData()

        searchFragment = SearchFragment()
        carsListFragment = CarsListFragment()
        favouriteListFragment = FavouriteListFragment()
        newsPreviewFragment = NewsPreviewFragment()


        setNavigationVisibiltity(true)
        navigation.setOnNavigationItemSelectedListener(this)

        if (App.instance?.isNetworkAvailable()!!) {
            navigation.visibility = View.VISIBLE
        } else {
            offLineText.visibility = View.VISIBLE
            offLineImage.visibility = View.VISIBLE
        }

        FragmentUtil.replaceFragment(supportFragmentManager, carsListFragment, false)


    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            com.vptarasov.autosearch.R.id.catalogue -> FragmentUtil.replaceFragment(supportFragmentManager, carsListFragment, false)
            com.vptarasov.autosearch.R.id.search_car -> {
                val bundle = Bundle()
                bundle.putSerializable("searchData", searchData)
                searchFragment.arguments = bundle
                FragmentUtil.replaceFragment(supportFragmentManager, searchFragment, false)
            }
            com.vptarasov.autosearch.R.id.favouriteList -> FragmentUtil.replaceFragment(supportFragmentManager, favouriteListFragment, false)
            com.vptarasov.autosearch.R.id.articles -> FragmentUtil.replaceFragment(supportFragmentManager, newsPreviewFragment, false)
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
    @SuppressLint("CheckResult")
    private fun loadData() {
        Api.service.loadData("")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ responseBody ->
                val html = responseBody.string()
                searchData = HTMLParser().getSearchData(html)

            }, { throwable -> throwable.printStackTrace() })
    }



}
