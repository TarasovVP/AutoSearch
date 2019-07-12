package com.vptarasov.autosearch.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper
import com.j256.ormlite.support.ConnectionSource
import com.j256.ormlite.table.TableUtils
import com.vptarasov.autosearch.model.Car
import java.sql.SQLException

class DatabaseHelper(context: Context) : OrmLiteSqliteOpenHelper(context,
    DB_NAME, null,
    DATABASE_VERSION
) {
    private var favoritesDao: FavoritesDao? = null

    override fun onCreate(db: SQLiteDatabase, connectionSource: ConnectionSource) {
        try {
            TableUtils.createTable<Car>(connectionSource, Car::class.java)
        } catch (e: SQLException) {
            Log.e(TAG, "error creating DB $DB_NAME")
            throw RuntimeException(e)
        }

    }

    override fun onUpgrade(db: SQLiteDatabase, connectionSource: ConnectionSource, oldVer: Int, newVer: Int) {
        try {
            TableUtils.dropTable<Car, Any>(connectionSource, Car::class.java, true)
            onCreate(db, connectionSource)
        } catch (e: SQLException) {
            Log.e(TAG, "error upgrading db " + DB_NAME + "from ver " + oldVer)
            throw RuntimeException(e)
        }

    }

    @Throws(SQLException::class)
    fun getFavoritesDao(): FavoritesDao {
        if (favoritesDao == null) {
            favoritesDao = FavoritesDao(
                getConnectionSource(),
                Car::class.java
            )
        }
        return favoritesDao as FavoritesDao
    }


    override fun close() {
        super.close()
        favoritesDao = null
    }

    companion object {

        private val TAG = DatabaseHelper::class.java.simpleName
        private const val DATABASE_VERSION = 1
        private const val DB_NAME = "car.db"
    }
}