package com.muhammadfaishalrizqipratama0094.cookit_.ui.screen

import android.graphics.Bitmap
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.muhammadfaishalrizqipratama0094.cookit_.ResepApplication
import com.muhammadfaishalrizqipratama0094.cookit_.model.Resep
import com.muhammadfaishalrizqipratama0094.cookit_.network.ResepApi
import com.muhammadfaishalrizqipratama0094.cookit_.repository.ResepRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream

enum class ApiStatus { LOADING, SUCCESS, FAILED }

class MainViewModel(private val repository: ResepRepository) : ViewModel() {

    val resepState: StateFlow<List<Resep>> = repository.allResep
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    var apiStatus = MutableStateFlow(ApiStatus.LOADING)
        private set
    var errorMessage = mutableStateOf<String?>(null)
        private set

    fun getResepById(resepId: String): Resep? {
        return resepState.value.find { it.id == resepId }
    }

    fun retrieveData(userId: String?) {
        viewModelScope.launch {
            apiStatus.value = ApiStatus.LOADING
            try {
                repository.refreshResep(userId)
                apiStatus.value = ApiStatus.SUCCESS
            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message}"
                apiStatus.value = ApiStatus.FAILED
            }
        }
    }

    fun saveData(
        userId: String,
        judul: String,
        deskripsi: String,
        bahan: String,
        instruksi: String,
        bitmap: Bitmap,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = ResepApi.service.postResep(
                    userId = userId,
                    judul = judul.toRequestBody("text/plain".toMediaTypeOrNull()),
                    deskripsi = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bahan = bahan.toRequestBody("text/plain".toMediaTypeOrNull()),
                    instruksi = instruksi.toRequestBody("text/plain".toMediaTypeOrNull()),
                    image = bitmap.toMultipartBody()
                )
                if (result.status == "success") {
                    repository.refreshResep(userId)
                    launch(Dispatchers.Main) { onSuccess() }
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                errorMessage.value = "Gagal menyimpan: ${e.message}"
            }
        }
    }

    fun updateData(
        userId: String,
        resepId: String,
        judul: String,
        deskripsi: String,
        bahan: String,
        instruksi: String,
        bitmap: Bitmap?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = ResepApi.service.updateResep(
                    userId = userId,
                    resepId = resepId,
                    judul = judul.toRequestBody("text/plain".toMediaTypeOrNull()),
                    deskripsi = deskripsi.toRequestBody("text/plain".toMediaTypeOrNull()),
                    bahan = bahan.toRequestBody("text/plain".toMediaTypeOrNull()),
                    instruksi = instruksi.toRequestBody("text/plain".toMediaTypeOrNull()),
                    image = bitmap?.toMultipartBody()
                )
                if (result.status == "success") {
                    repository.refreshResep(userId)
                    launch(Dispatchers.Main) { onSuccess() }
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                errorMessage.value = "Gagal mengupdate: ${e.message}"
            }
        }
    }

    fun deleteData(userId: String, resepId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = ResepApi.service.deleteResep(userId, resepId)
                if (result.status == "success") {
                    repository.refreshResep(userId)
                } else {
                    throw Exception(result.message)
                }
            } catch (e: Exception) {
                errorMessage.value = "Gagal menghapus: ${e.message}"
            }
        }
    }

    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody("image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData("image", "image.jpg", requestBody)
    }

    fun clearMessage() {
        errorMessage.value = null
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = (checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as ResepApplication)
                return MainViewModel(application.container.resepRepository) as T
            }
        }
    }
}