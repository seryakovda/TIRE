package com.example.tire.data.brand


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BrandDao {
    @Query("SELECT * from brand_list")
    fun getAllBrand(): List<BrandItem>

    @Query("SELECT * from brand_list where name = :name")
    suspend fun getByIdBrand(name: String) : BrandItem?

    @Insert
    suspend fun insertBrand(item:BrandItem)

    @Update
    suspend fun updateBrand(item:BrandItem)

    @Delete
    suspend fun deleteBrand(item:BrandItem)

    @Query("DELETE FROM brand_list")
    suspend fun deleteAllBrand()

}