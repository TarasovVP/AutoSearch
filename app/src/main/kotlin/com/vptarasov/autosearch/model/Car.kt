package com.vptarasov.autosearch.model

import com.google.firebase.auth.FirebaseAuth
import java.io.Serializable

class Car : Serializable {
    var name: String? = ""
    var price: String? = ""
    var year: String? = ""
    var photo: String? = ""
    var engine: String? = ""
    var mileage: String? = ""
    var color: String? = ""
    var gearbox: String? = ""
    var drive: String? = ""
    var body: String? = ""
    var city: String? = ""
    var date: String? = ""
    var url: String? = ""
    var phone: String? = ""
    var photoSeller: String? = ""
    var photoList: ArrayList<String>? = ArrayList()
    var webMainText: String? = ""
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
        return urlToId() + FirebaseAuth.getInstance().currentUser?.uid == car?.urlToId() + FirebaseAuth.getInstance().currentUser?.uid
    }

   /* @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun hashCode(): Int {
        return Objects.hash(urlToId() + FirebaseAuth.getInstance().currentUser?.uid)
    }*/

}
