package com.vptarasov.autosearch.api

class GetResponseBody
    : BaseUseCase<GetResponseBody.Params, GetResponseBody.Result>() {

    override suspend fun loadCar(urlCar: String): Result {
        return Result(
            ApiService()
                .doWork(ApiService.Params()).loadCarAsync(urlCar).await().string()
        )
    }

    override suspend fun loadSearchData(url: String): Result {
        return Result(
            ApiService()
                .doWork(ApiService.Params()).loadSearchDataAsync(url).await().string()
        )
    }

    override suspend fun getModel(url: String): Result {
        return Result(
            ApiService()
                .doWork(ApiService.Params()).getModelAsync(url).await().string()
        )
    }

    override suspend fun getCity(url: String): Result {
        return Result(
            ApiService()
                .doWork(ApiService.Params()).getCityAsync(url).await().string()
        )
    }



    override suspend fun loadCars(mark: String?, model: String?, region: String?, city: String?, body: String?, color: String?, engineFrom: String?, engineUnit: String?, yearFrom: String?, engineTo: String?, yearTo: String?, priceFrom: String?,
                                  priceTo: String?, petrolElectro: String?, diesel: String?, electro: String?, gas: String?, gasPetrol: String?, petrol: String?, gearboxAutom: String?, gearboxMech: String?, page: String?): Result {

        return Result(
            ApiService()
                .doWork(ApiService.Params()).loadCarsAsync(mark, model, region, city, body, color, engineFrom, engineUnit, yearFrom, engineTo, yearTo, priceFrom,
                    priceTo, petrolElectro, diesel, electro, gas, gasPetrol, petrol, gearboxAutom, gearboxMech, page).await().string()
        )
    }


    class Params
    class Result(val responseBody: String?)
}