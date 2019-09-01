package com.vptarasov.autosearch.api

import com.google.gson.Gson
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.vptarasov.autosearch.util.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class ApiService :
    BaseApi<ApiService.Params, ApiService.Result>() {

    override suspend fun doWork(params: Params): ApiInterface {

        val interceptor = LogginInterceptor()

        val client = OkHttpClient.Builder()
            .followRedirects(false)
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .addInterceptor(interceptor).build()

        return Retrofit
            .Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(ApiInterface::class.java)
    }

    class Params
    data class Result(val apiInterface: ApiInterface?)

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
}