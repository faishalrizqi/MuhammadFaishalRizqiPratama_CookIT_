package com.muhammadfaishalrizqipratama0094.cookit_.repository

import android.util.Log
import com.muhammadfaishalrizqipratama0094.cookit_.db.ResepDao
import com.muhammadfaishalrizqipratama0094.cookit_.model.Resep
import com.muhammadfaishalrizqipratama0094.cookit_.network.ResepApiService
import kotlinx.coroutines.flow.Flow

class ResepRepository(
    private val resepDao: ResepDao,
    private val resepApiService: ResepApiService
) {
    val allResep: Flow<List<Resep>> = resepDao.getAllResep()

    suspend fun refreshResep(userId: String?) {
        try {
            val networkResep = resepApiService.getResep(userId)
            resepDao.deleteAll()
            resepDao.insertAll(networkResep)
            Log.d("ResepRepository", "Successfully fetched and saved ${networkResep.size} recipes.")
        } catch (e: Exception) {
            Log.e("ResepRepository", "Failed to refresh resep: ${e.message}")
        }
    }
}