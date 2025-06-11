package com.muhammadfaishalrizqipratama0094.cookit_.model

import com.squareup.moshi.Json

data class Resep(
    val id: String,
    val judul: String,
    val deskripsi: String,
    @Json(name = "bahanBahan") val bahan: String,
    val instruksi: String,
    val imageId: String?,
    val mine: Int
)