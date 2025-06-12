package com.muhammadfaishalrizqipratama0094.cookit_.db

import androidx.room.*
import com.muhammadfaishalrizqipratama0094.cookit_.model.Resep
import kotlinx.coroutines.flow.Flow

@Dao
interface ResepDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(resep: List<Resep>)

    @Query("SELECT * FROM resep ORDER BY judul ASC")
    fun getAllResep(): Flow<List<Resep>>

    @Query("DELETE FROM resep")
    suspend fun deleteAll()
}