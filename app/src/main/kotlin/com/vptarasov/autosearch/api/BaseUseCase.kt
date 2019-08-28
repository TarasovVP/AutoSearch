package com.vptarasov.autosearch.api

abstract class BaseUseCase<Params, Result> {

    abstract suspend fun loadCar(params: Params, urlCar: String): Result

    abstract suspend fun loadSearchData(params: Params, url: String): Result

    abstract suspend fun getModel(params: Params, url: String): Result

    abstract suspend fun getCity(params: Params, url: String): Result

    abstract suspend fun loadCars(params: GetResponseBody.Params, mark: String?, model: String?, region: String?, city: String?, body: String?, color: String?, engineFrom: String?, engineUnit: String?, yearFrom: String?, engineTo: String?, yearTo: String?, priceFrom: String?,
                                  priceTo: String?, petrolElectro: String?, diesel: String?, electro: String?, gas: String?, gasPetrol: String?, petrol: String?, gearboxAutom: String?, gearboxMech: String?, page: String?): Result

}