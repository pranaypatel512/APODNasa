package com.pranay.apodnasa.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pranay.apodnasa.model.APODPictureItem

object DatabaseMigrations {
    const val DB_VERSION = 2

    val MIGRATIONS: Array<Migration>
        get() = arrayOf(
            migration12()
        )

    private fun migration12(): Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE ${APODPictureItem.TABLE_NAME} ADD COLUMN thumbnailUrl TEXT")
        }
    }
}
