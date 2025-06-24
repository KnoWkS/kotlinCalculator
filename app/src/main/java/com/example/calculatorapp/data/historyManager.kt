package com.example.calculatorapp.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private val Context.dataStore by preferencesDataStore(name = "calculator_history")

object HistoryManager {
    private val HISTORY_KEY = stringPreferencesKey("history")

    fun getHistory(context: Context): Flow<List<String>> {
        return context.dataStore.data.map { preferences ->
            val json = preferences[HISTORY_KEY] ?: return@map emptyList()
            runCatching {
                Json.decodeFromString<List<String>>(json)
            }.getOrElse { emptyList() }
        }
    }

    suspend fun saveHistory(context: Context, history: List<String>) {
        val json = Json.encodeToString(history)
        context.dataStore.edit { preferences ->
            preferences[HISTORY_KEY] = json
        }
    }
    suspend fun clearHistory(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(HISTORY_KEY)
        }
    }
}
