package com.vptarasov.autosearch.api

import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiInterface {

    @GET
    fun loadCarAsync(@Url urlCar: String?): Deferred<ResponseBody>

    @GET("search-car/?onp=20")
    fun loadSearchDataAsync(@Query(value = "w", encoded = true) page: String): Deferred<ResponseBody>

    @GET("search-car/?onp=20")
    fun getModelAsync(@Query(value = "m[]") mark: String?): Deferred<ResponseBody>

    @GET("search-car/?onp=20")
    fun getCityAsync(@Query(value = "o[]") mark: String?): Deferred<ResponseBody>

    @GET("search-car/?onp=20")
    fun loadCarsAsync(
        @Query(value = "m[]") mark: String?,
        @Query(value = "n[]") model: String?,
        @Query(value = "o[]") region: String?,
        @Query(value = "c[]") city: String?,
        @Query(value = "k[]") body: String?,
        @Query(value = "co[]") color: String?,
        @Query(value = "m1") engineFrom: String?,
        @Query(value = "pr") engineUnit: String?,
        @Query(value = "y1") yearFrom: String?,
        @Query(value = "m2") engineTo: String?,
        @Query(value = "y2") yearTo: String?,
        @Query(value = "p1") priceFrom: String?,
        @Query(value = "p2") priceTo: String?,
        @Query(value = "f[]") petrolElectro: String?,
        @Query(value = "f[]") diesel: String?,
        @Query(value = "f[]") electro: String?,
        @Query(value = "f[]") gas: String?,
        @Query(value = "f[]") gasPetrol: String?,
        @Query(value = "f[]") petrol: String?,
        @Query(value = "g[]") gearboxAutom: String?,
        @Query(value = "g[]") gearboxMech: String?,
        @Query(value = "w") page: String?
    ): Deferred<ResponseBody>


}