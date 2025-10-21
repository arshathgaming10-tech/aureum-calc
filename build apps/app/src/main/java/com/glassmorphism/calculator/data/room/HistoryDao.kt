package com.glassmorphism.calculator.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history_entries ORDER BY timestamp DESC")
    fun allEntries(): Flow<List<HistoryEntry>>

    @Insert
    suspend fun insert(entry: HistoryEntry)

    @Insert
    suspend fun insertAll(entries: List<HistoryEntry>)

    @androidx.room.Transaction
    suspend fun replaceAll(entries: List<HistoryEntry>) {
        clearAll()
        if (entries.isNotEmpty()) insertAll(entries)
    }

    @Query("DELETE FROM history_entries")
    suspend fun clearAll()
}
