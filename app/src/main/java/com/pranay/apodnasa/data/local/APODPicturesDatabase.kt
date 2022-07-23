package com.pranay.apodnasa.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.pranay.apodnasa.data.local.dao.APODPictureDao
import com.pranay.apodnasa.model.APODPictureItem

/**
 * Abstract APOD Pictures Database.
 * It provides DAO [APODPictureDao] by using method [getPicturesDao].
 */
@Database(
    entities = [APODPictureItem::class],
    version = DatabaseMigrations.DB_VERSION
)
abstract class APODPicturesDatabase : RoomDatabase() {

    /**
     * @return [APODPictureDao] Pictures Data Access Object.
     */
    abstract fun getPicturesDao(): APODPictureDao

    companion object {
        const val DB_NAME = "apod_pictures_database"

        @Volatile
        private var INSTANCE: APODPicturesDatabase? = null

        fun getInstance(context: Context): APODPicturesDatabase {
            val apodPicturesDatabase = INSTANCE
            if (apodPicturesDatabase != null) {
                return apodPicturesDatabase
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    APODPicturesDatabase::class.java,
                    DB_NAME
                ).addMigrations(*DatabaseMigrations.MIGRATIONS).build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
