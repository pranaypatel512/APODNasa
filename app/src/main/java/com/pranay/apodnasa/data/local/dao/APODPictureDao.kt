package com.pranay.apodnasa.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pranay.apodnasa.model.APODPictureItem
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for [com.pranay.apodnasa.data.local.APODPicturesDatabase]
 */
@Dao
interface APODPictureDao {

    /**
     * Inserts [pictures] into the [APODPictureItem.TABLE_NAME] table.
     * Duplicate values are replaced in the table.
     * @param pictures APODPictureItem
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPictures(pictures: List<APODPictureItem>)

    /**
     * Deletes all the pictures from the [APODPictureItem.TABLE_NAME] table.
     */
    @Query("DELETE FROM ${APODPictureItem.TABLE_NAME}")
    suspend fun deleteAllPictures()

    /**
     * Fetches the pictures from the [APODPictureItem.TABLE_NAME] table
     * whose url is [imageUrl].
     * @param imageUrl picture url of [APODPictureItem]
     * @return [Flow] of [APODPictureItem] from database table.
     */
    @Query("SELECT * FROM ${APODPictureItem.TABLE_NAME} WHERE url = :imageUrl")
    fun getPictureByUrl(imageUrl: String): Flow<APODPictureItem>

    /**
     * Fetches all the pictures from the [APODPictureItem.TABLE_NAME] table.
     * @return [Flow]
     */
    @Query("SELECT * FROM ${APODPictureItem.TABLE_NAME}")
    fun getAllPictures(): Flow<List<APODPictureItem>>
}
