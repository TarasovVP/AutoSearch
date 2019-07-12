package com.vptarasov.autosearch.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.vptarasov.autosearch.R
import com.vptarasov.autosearch.ui.fragments.LoadingFragment
import com.vptarasov.autosearch.util.FragmentUtil

class SplashScreenActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FragmentUtil.replaceFragment(supportFragmentManager, LoadingFragment(), false)
    }


}
