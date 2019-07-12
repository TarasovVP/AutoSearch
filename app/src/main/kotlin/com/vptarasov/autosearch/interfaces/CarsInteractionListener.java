package com.vptarasov.autosearch.interfaces;


import com.vptarasov.autosearch.model.Car;

public interface CarsInteractionListener {
    void onLastItemReached();
    void onLastItemLeft();
    void onItemClick(Car car);
    void onFavoriteClick(Car car);
}
