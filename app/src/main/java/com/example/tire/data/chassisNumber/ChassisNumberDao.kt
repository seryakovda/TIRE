package com.example.tire.data.chassisNumber

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface ChassisNumberDao {
    @Query("SELECT * from ChassisNumber_list")
    fun getAllChassisNumber(): List<ChassisNumberItem>

    @Query("SELECT * from ChassisNumber_list where name = :name")
    fun getByIdChassisNumber(name: String) : ChassisNumberItem?

    @Insert
    suspend fun insertChassisNumber(item:ChassisNumberItem)

    @Update
    suspend fun updateChassisNumber(item:ChassisNumberItem)

    @Delete
    suspend fun deleteChassisNumber(item:ChassisNumberItem)

    @Query("DELETE FROM ChassisNumber_list")
    suspend fun deleteAllChassisNumber()

}