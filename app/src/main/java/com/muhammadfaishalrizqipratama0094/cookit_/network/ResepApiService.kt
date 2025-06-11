package com.muhammadfaishalrizqipratama0094.cookit_.network

import com.muhammadfaishalrizqipratama0094.cookit_.model.OpStatus
import com.muhammadfaishalrizqipratama0094.cookit_.model.Resep
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

private const val BASE_URL = "https://resep.bagasaldianata.my.id/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface ResepApiService {
    @GET("resep.php")
    suspend fun getResep(
        @Header("Authorization") userId: String?
    ): List<Resep>

    @Multipart
    @POST("resep.php")
    suspend fun postResep(
        @Header("Authorization") userId: String,
        @Part("judul") judul: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("bahanBahan") bahan: RequestBody,
        @Part("instruksi") instruksi: RequestBody,
        @Part image: MultipartBody.Part
    ): OpStatus

    @Multipart
    @PUT("resep.php")
    suspend fun updateResep(
        @Header("Authorization") userId: String,
        @Query("id") resepId: String,
        @Part("judul") judul: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("bahanBahan") bahan: RequestBody,
        @Part("instruksi") instruksi: RequestBody,
        @Part image: MultipartBody.Part?
    ): OpStatus

    @DELETE("resep.php")
    suspend fun deleteResep(
        @Header("Authorization") userId: String,
        @Query("id") resepId: String
    ): OpStatus
}

object ResepApi {
    val service: ResepApiService by lazy {
        retrofit.create(ResepApiService::class.java)
    }
    fun getImageUrl(imageId: String?): String {
        return "${BASE_URL}image.php?id=${imageId ?: ""}"
    }
}