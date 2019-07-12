package com.vptarasov.autosearch.model


import android.os.Build
import androidx.annotation.RequiresApi
import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import java.io.Serializable
import java.util.*

@DatabaseTable
class Car : Serializable {
    @DatabaseField
    var name: String? = null
    @DatabaseField
    var price: String? = null
    @DatabaseField
    var year: String? = null
    @DatabaseField
    var photo: String? = null
    @DatabaseField
    var engine: String? = null
    @DatabaseField
    var mileage: String? = null
    @DatabaseField
    var color: String? = null
    @DatabaseField
    var gearbox: String? = null
    @DatabaseField
    var drive: String? = null
    @DatabaseField
    var body: String? = null
    @DatabaseField
    var city: String? = null
    @DatabaseField
    var date: String? = null
    @DatabaseField
    var url: String? = null
    @DatabaseField
    var phone: String? = null
    @DatabaseField
    var photoSeller: String? = null
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    var photoList: ArrayList<String>? = null
    @DatabaseField
    var webMainText: String? = null
    private var bookmarked: Boolean = false

    @DatabaseField(id = true)
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
