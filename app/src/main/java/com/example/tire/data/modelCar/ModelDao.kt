package com.example.tire.data.modelCar

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ModelDao {
    @Query("SELECT * from modelCar_list")
    fun getAllModel(): List<ModelItem>

    @Query("SELECT * from modelCar_list where name = :name")
    fun getByIdModel(name: String) : ModelItem?

    @Insert
    suspend fun insertModel(item:ModelItem)

    @Update
    suspend fun updateModel(item:ModelItem)

    @Delete
    suspend fun deleteModel(item:ModelItem)

    @Query("DELETE FROM modelCar_list")
    suspend fun deleteAllModel()

}

