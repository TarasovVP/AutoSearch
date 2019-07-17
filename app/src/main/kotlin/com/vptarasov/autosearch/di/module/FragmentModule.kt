package com.vptarasov.autosearch.di.module

import com.vptarasov.autosearch.ui.fragments.car.CarContract
import com.vptarasov.autosearch.ui.fragments.car.CarPresenter
import com.vptarasov.autosearch.ui.fragments.cars_list.CarsListContract
import com.vptarasov.autosearch.ui.fragments.cars_list.CarsListPresenter
import com.vptarasov.autosearch.ui.fragments.favourite.FavouriteContract
import com.vptarasov.autosearch.ui.fragments.favourite.FavouritePresenter
import com.vptarasov.autosearch.ui.fragments.loading.LoadingContract
import com.vptarasov.autosearch.ui.fragments.loading.LoadingPresenter
import com.vptarasov.autosearch.ui.fragments.news.NewsContract
import com.vptarasov.autosearch.ui.fragments.news.NewsPresenter
import com.vptarasov.autosearch.ui.fragments.news_preview.NewsPreviewContract
import com.vptarasov.autosearch.ui.fragments.news_preview.NewsPreviewPresenter
import com.vptarasov.autosearch.ui.fragments.photo_full_size.PhotoFullSizeContract
import com.vptarasov.autosearch.ui.fragments.photo_full_size.PhotoFullSizePresenter
import com.vptarasov.autosearch.ui.fragments.search.SearchContract
import com.vptarasov.autosearch.ui.fragments.search.SearchPresenter
import dagger.Module
import dagger.Provides

@Module
class FragmentModule {
    @Provides
    fun provideLoadingPresenter(): LoadingContract.Presenter {
        return LoadingPresenter()
    }
    @Provides
    fun provideCarsListPresenter(): CarsListContract.Presenter {
        return CarsListPresenter()
    }
    @Provides
    fun provideCarPresenter(): CarContract.Presenter {
        return CarPresenter()
    }
    @Provides
    fun providePhotoFullSizePresenter(): PhotoFullSizeContract.Presenter {
        return PhotoFullSizePresenter()
    }
    @Provides
    fun provideSearchPresenter(): SearchContract.Presenter {
        return SearchPresenter()
    }
    @Provides
    fun provideFavouritePresenter(): FavouriteContract.Presenter {
        return FavouritePresenter()
    }
    @Provides
    fun provideNewsPreviewPresenter(): NewsPreviewContract.Presenter {
        return NewsPreviewPresenter()
    }
    @Provides
    fun provideNewsPresenter(): NewsContract.Presenter {
        return NewsPresenter()
    }
}