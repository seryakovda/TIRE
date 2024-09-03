package com.example.tire.data.standard_size

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StandardSizeDao {
    @Query("SELECT * from StandardSize_list")
    fun getAllStandardSize(): List<StandardSizeItem>

    @Query("SELECT * from StandardSize_list where name = :name")
    fun getByIdStandardSize(name: String) : StandardSizeItem?

    @Insert
    suspend fun insertStandardSize(item:StandardSizeItem)

    @Update
    suspend fun updateStandardSize(item:StandardSizeItem)

    @Delete
    suspend fun deleteStandardSize(item:StandardSizeItem)

    @Query("DELETE FROM StandardSize_list")
    suspend fun deleteAllStandardSize()

}