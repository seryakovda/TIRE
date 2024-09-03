package com.example.tire.data.division

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface DivisionDao {
    @Query("SELECT * from Division_list")
    fun getAllDivision(): List<DivisionItem>

    @Query("SELECT * from Division_list where name = :name")
    fun getByIdDivision(name: String) : DivisionItem?

    @Insert
    suspend fun insertDivision(item:DivisionItem)

    @Update
    suspend fun updateDivision(item:DivisionItem)

    @Delete
    suspend fun deleteDivision(item:DivisionItem)

    @Query("DELETE FROM Division_list")
    suspend fun deleteAllDivision()

}