package com.vptarasov.autosearch.interfaces;


import com.vptarasov.autosearch.model.Car;

public interface FavoritesInteractionListener {
    void onItemClick(Car car);
    void onFavoriteClick(Car car);
    void onLastFavoriteRemoved();

}
