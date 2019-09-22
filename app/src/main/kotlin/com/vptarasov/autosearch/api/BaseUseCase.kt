package com.vptarasov.autosearch.api

abstract class BaseUseCase<Params, Result> {

    abstract suspend fun loadCar(urlCar: String): Result

    abstract suspend fun loadSearchData(url: String): Result

    abstract suspend fun getModel(url: String): Result
    abstract suspend fun getCity(url: String): Result

    abstract suspend fun loadCars(mark: String?, model: String?, region: String?, city: String?, body: String?, color: String?, engineFrom: String?, engineUnit: String?, yearFrom: String?, engineTo: String?, yearTo: String?, priceFrom: String?,
                                  priceTo: String?, petrolElectro: String?, diesel: String?, electro: String?, gas: String?, gasPetrol: String?, petrol: String?, gearboxAutom: String?, gearboxMech: String?, page: String?): Result

}