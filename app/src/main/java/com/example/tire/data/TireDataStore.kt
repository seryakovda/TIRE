package com.example.tire.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

class TireDataStore @Inject constructor( @ApplicationContext
                                         private val context: Context,
){
    companion object {

        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("ItemTire")
        private val USER_TOKEN_KEY = stringPreferencesKey("ItemTire")

    }

    val getAccessToken: Flow<TireList?> = context.dataStore.data.map {
        preferences ->
        preferences[USER_TOKEN_KEY]?.let {
            Json.decodeFromString<TireList>(it)
        }
    }

    suspend fun saveToken(tireList:TireList?) {
        val vv = Json.encodeToString(tireList)
        context.dataStore.edit { preferences ->
            preferences[USER_TOKEN_KEY] = vv
        }
    }

    suspend fun setValueBrand(value:String){
        var TireList = getAccessToken.collect{
            if (it != null) {
                it.brand = value
                saveToken(it)
            }
        }
    }

    suspend fun setValueStandardSize(value:String){
        var TireList = getAccessToken.collect{
            if (it != null) {
                it.standardSize = value
                saveToken(it)
            }
        }
    }



    suspend fun setValueModel(value:String){
        var TireList = getAccessToken.collect{
            if (it != null) {
                it.model = value
                saveToken(it)
            }
        }
    }



    suspend fun setValueDivision(value:String){
        var TireList = getAccessToken.collect{
            if (it != null) {
                it.division = value
                saveToken(it)
            }
        }
    }



    suspend fun setValueChassisNumber(value:String){
        var TireList = getAccessToken.collect{
            if (it != null) {
                it.chassisNumber = value
                saveToken(it)
            }
        }
    }
}