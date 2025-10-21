package com.glassmorphism.calculator.data

import android.content.Context
import androidx.room.Room
import com.glassmorphism.calculator.data.room.AppDatabase
import com.glassmorphism.calculator.data.room.HistoryEntry
import kotlinx.coroutines.flow.Flow

class HistoryRepository private constructor(context: Context) {
    private val db = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "aureum-db").build()
    private val dao = db.historyDao()

    fun allEntries(): Flow<List<HistoryEntry>> = dao.allEntries()

    suspend fun insert(entry: HistoryEntry) = dao.insert(entry)

    suspend fun insertAll(entries: List<HistoryEntry>) = dao.insertAll(entries)

    suspend fun replaceAll(entries: List<HistoryEntry>) = dao.replaceAll(entries)

    suspend fun clearAll() = dao.clearAll()

    companion object {
        @Volatile
        private var INSTANCE: HistoryRepository? = null

        fun getInstance(context: Context): HistoryRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: HistoryRepository(context).also { INSTANCE = it }
        }
    }
}
