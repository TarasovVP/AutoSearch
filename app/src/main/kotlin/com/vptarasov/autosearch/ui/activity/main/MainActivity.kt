package com.vptarasov.autosearch.ui.activity.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.vptarasov.autosearch.App
import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.di.component.DaggerActivityComponent
import com.vptarasov.autosearch.di.module.ActivityModule
import com.vptarasov.autosearch.model.SearchData
import com.vptarasov.autosearch.ui.fragments.cars_list.CarsListFragment
import com.vptarasov.autosearch.ui.fragments.favourite.FavouriteFragment
import com.vptarasov.autosearch.ui.fragments.news_preview.NewsPreviewFragment
import com.vptarasov.autosearch.ui.fragments.search.SearchFragment
import com.vptarasov.autosearch.util.FragmentUtil
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.user_info.*
import javax.inject.Inject


open class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    MainContract.View {

    @Inject
    lateinit var presenter: MainContract.Presenter

    private val code = 0
    private var userNameTV: TextView? = null

    private lateinit var searchFragment: Fragment
    private lateinit var carsListFragment: Fragment
    private lateinit var favouriteListFragment: Fragment
    private lateinit var newsPreviewFragment: Fragment
    private lateinit var searchData: SearchData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        injectDependency()

        if (FirebaseAuth.getInstance().currentUser == null) {
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(),
                code
            )
        } else {
           val name = FirebaseAuth.getInstance()
                .currentUser
                ?.displayName
            Toast.makeText(
                this,
                "Добро пожаловать $name",
                Toast.LENGTH_LONG
            )
                .show()

        }

        val toolbar  = toolbar
        toolbar.visibility = View.VISIBLE
        toolbar.inflateMenu(R.menu.main_menu)
        toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.menu_sign_out) {
                AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener {
                        Toast.makeText(
                            this@MainActivity,
                            "Вы отписались",
                            Toast.LENGTH_LONG
                        )
                            .show()

                        finish()
                    }
            }

            false
        }

        searchFragment = SearchFragment()
        carsListFragment = CarsListFragment()
        favouriteListFragment = FavouriteFragment()
        newsPreviewFragment = NewsPreviewFragment()

        userNameTV = userName
        userNameTV?.text = FirebaseAuth.getInstance()
            .currentUser
            ?.displayName
        val intent = intent
        searchData = (intent.getSerializableExtra("searchData") as? SearchData)!!

        presenter.attach(this)
        setNavigationVisibiltity(true)
        navigation.setOnNavigationItemSelectedListener(this)


        if (App.instance?.isNetworkAvailable()!!) {
            navigation.visibility = View.VISIBLE
        } else {
            offLineText.visibility = View.VISIBLE
            offLineImage.visibility = View.VISIBLE
        }

    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == code) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(
                    this,
                    "Вы успешно зарегистрированы.",
                    Toast.LENGTH_LONG
                )
                    .show()
                userNameTV?.text = FirebaseAuth.getInstance()
                    .currentUser
                    ?.displayName
            } else {
                Toast.makeText(
                    this,
                    "Процесс регистрации прошел неудачно. Попробуйте позже.",
                    Toast.LENGTH_LONG
                )
                    .show()
                finish()
            }
        }

    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.catalogue -> showCarsListFragment()
            R.id.search_car -> showSearchFragment()
            R.id.favouriteList -> showFavouriteListFragment()
            R.id.articles -> showNewsPreviewFragment()
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
        builder.setMessage(getString(R.string.want_to_exit_app))
        builder.setCancelable(true)

        builder.setPositiveButton(
            resources.getString(R.string.Yes)
        ) { _, _ -> finish() }

        builder.setNegativeButton(
            resources.getString(R.string.No)
        ) { dialog, _ -> dialog.cancel() }

        val alert = builder.create()
        alert.show()


    }

    override fun showCarsListFragment() {
        FragmentUtil.replaceFragment(supportFragmentManager, carsListFragment, false)
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


}
