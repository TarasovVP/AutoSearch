package com.vptarasov.autosearch.model

import java.io.Serializable

data class QueryDetails(
    var mark: String? = null,
    var model: String? = null,
    var region: String? = null,
    var city: String? = null,
    var body: String? = null,
    var color: String? = null,
    var engineFrom: String? = null,
    var engineUnit: String? = null,
    var yearFrom: String? = null,
    var engineTo: String? = null,
    var yearTo: String? = null,
    var priceTo: String? = null,
    var priceFrom: String? = null,
    var petrolElectro: String? = null,
    var diesel: String? = null,
    var electro: String? = null,
    var gas: String? = null,
    var gasPetrol: String? = null,
    var petrol: String? = null,
    var gearboxAutom: String? = null,
    var gearboxMech: String? = null
) : Serializable


