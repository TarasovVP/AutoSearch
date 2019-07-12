package com.vptarasov.autosearch.database

import com.j256.ormlite.dao.BaseDaoImpl
import com.j256.ormlite.support.ConnectionSource
import com.vptarasov.autosearch.model.Car
import java.sql.SQLException
import java.util.*

class FavoritesDao @Throws(SQLException::class)
internal constructor(connectionSource: ConnectionSource, dataClass: Class<Car>) : BaseDaoImpl<Car, String>(connectionSource, dataClass) {

    fun exists(car: Car?): Boolean {
        try {
            return idExists(car?.urlToId())
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return false
    }

    fun switchBookmarkedStatus(car: Car?) {
        try {
            if (exists(car)) {
                delete(car)
            } else {
                car?.id = car?.urlToId()
                createOrUpdate(car)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }

    }

    fun loadAll(): List<Car> {
        try {
            return queryForAll()
        } catch (e: SQLException) {
            e.printStackTrace()
        }

        return ArrayList()
    }
}
