package com.vptarasov.autosearch.di.component

import com.vptarasov.autosearch.di.module.FragmentModule
import com.vptarasov.autosearch.ui.fragments.car.CarFragment
import com.vptarasov.autosearch.ui.fragments.cars_list.CarsListFragment
import com.vptarasov.autosearch.ui.fragments.favourite.FavouriteFragment
import com.vptarasov.autosearch.ui.fragments.news.NewsFragment
import com.vptarasov.autosearch.ui.fragments.news_preview.NewsPreviewFragment
import com.vptarasov.autosearch.ui.fragments.photo_full_size.PhotoFullSizeFragment
import com.vptarasov.autosearch.ui.fragments.search.SearchFragment
import dagger.Component

@Component(modules = [FragmentModule::class])
interface FragmentComponent {

    fun inject(carsListFragment: CarsListFragment)
    fun inject(carFragment: CarFragment)
    fun inject(photoFullSizeFragment: PhotoFullSizeFragment)
    fun inject(searchFragment: SearchFragment)
    fun inject(favouriteFragment: FavouriteFragment)
    fun inject(newsPreviewFragment: NewsPreviewFragment)
    fun inject(newsFragment: NewsFragment)

}