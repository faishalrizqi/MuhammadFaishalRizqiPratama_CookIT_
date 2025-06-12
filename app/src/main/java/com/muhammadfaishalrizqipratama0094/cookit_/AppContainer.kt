package com.muhammadfaishalrizqipratama0094.cookit_

import android.content.Context
import com.muhammadfaishalrizqipratama0094.cookit_.db.ResepDb
import com.muhammadfaishalrizqipratama0094.cookit_.network.ResepApi
import com.muhammadfaishalrizqipratama0094.cookit_.repository.ResepRepository

interface AppContainer {
    val resepRepository: ResepRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    override val resepRepository: ResepRepository by lazy {
        ResepRepository(
            ResepDb.getDatabase(context).resepDao(),
            ResepApi.service
        )
    }
}