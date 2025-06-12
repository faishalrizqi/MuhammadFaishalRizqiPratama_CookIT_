package com.muhammadfaishalrizqipratama0094.cookit_.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.muhammadfaishalrizqipratama0094.cookit_.model.Resep

@Database(entities = [Resep::class], version = 1, exportSchema = false)
abstract class ResepDb : RoomDatabase() {
    abstract fun resepDao(): ResepDao

    companion object {
        @Volatile
        private var Instance: ResepDb? = null

        fun getDatabase(context: Context): ResepDb {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ResepDb::class.java, "resep_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}