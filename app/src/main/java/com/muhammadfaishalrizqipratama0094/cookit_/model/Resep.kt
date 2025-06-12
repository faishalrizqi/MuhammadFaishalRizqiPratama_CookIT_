package com.muhammadfaishalrizqipratama0094.cookit_.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "resep")
data class Resep(
    @PrimaryKey val id: String,
    val judul: String,
    val deskripsi: String,
    @Json(name = "bahanBahan") val bahan: String,
    val instruksi: String,
    val imageId: String?,
    val mine: Int
)