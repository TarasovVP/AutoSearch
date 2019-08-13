package com.vptarasov.autosearch.model


import android.os.Build
import androidx.annotation.RequiresApi
import java.io.Serializable
import java.util.*

class Car : Serializable {

    var name: String? = null

    var price: String? = null

    var year: String? = null

    var photo: String? = null

    var engine: String? = null

    var mileage: String? = null

    var color: String? = null

    var gearbox: String? = null

    var drive: String? = null

    var body: String? = null

    var city: String? = null

    var date: String? = null

    var url: String? = null

    var phone: String? = null

    var photoSeller: String? = null

    var photoList: ArrayList<String>? = null

    var webMainText: String? = null
    private var bookmarked: Boolean = false

    var user: String? = null

    var id: String? = null

    fun urlToId(): String {
        return url?.replace("/", "")!!.replace(".", "").replace("-", "").replace("_", "")
    }

    fun isBookmarked(): Boolean {
        return bookmarked
    }

    fun setBookmarked(bookmarked: Boolean) {
        this.bookmarked = bookmarked
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val car = other as Car?
        return id == car?.id
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun hashCode(): Int {
        return Objects.hash(id)
    }

}
