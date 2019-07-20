package com.vptarasov.autosearch.api

import com.google.gson.Gson
import com.vptarasov.autosearch.util.Constants
import io.reactivex.Observable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import java.io.IOException
import java.util.concurrent.TimeUnit

interface Api {

    @GET
    fun loadUrl(@Url url: String?): Observable<ResponseBody>

    @GET("search-car/?onp=20")
    fun loadSearchData(@Query(value = "w", encoded = true) page: String): Observable<ResponseBody>

    @GET("search-car/?onp=20")
    fun getModel(@Query(value = "m[]") mark: String?): Observable<ResponseBody>

    @GET("search-car/?onp=20")
    fun getCity(@Query(value = "o[]") mark: String?): Observable<ResponseBody>

    @GET("search-car/?onp=20")
    fun loadCars(
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
    ): Observable<ResponseBody>


    class LogginInterceptor : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val t1 = System.nanoTime()
            println(
                String.format("Sending request %s on %n%s", request.url(), request.headers())
            )
            val response = chain.proceed(request)
            val t2 = System.nanoTime()
            println(
                String.format(
                    "Received response for %s in %.1fms%n%s", response.request().url(),
                    (t2 - t1) / 1e6, response.headers()
                )
            )
            return response
        }

    }

    companion object {

        private val interceptor = LogginInterceptor()

        private val client = OkHttpClient.Builder()
            .followRedirects(false)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(interceptor).build()

        private val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()

        val service = retrofit.create(Api::class.java)!!

    }


}