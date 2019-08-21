package com.vptarasov.autosearch.model

import java.io.Serializable

data class User(var id: String = "", var name: String = "", var email: String = "", var phoneNumber: String = "", var photoUrl: String = "", var cars: ArrayList<Car> = ArrayList()) :
    Serializable