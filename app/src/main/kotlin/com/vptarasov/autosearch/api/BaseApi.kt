package com.vptarasov.autosearch.api

abstract class BaseApi<Params, Result> {

    abstract suspend fun doWork(params: Params): ApiInterface

}