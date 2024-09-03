package com.example.tire.data.brand

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "brand_list")
@Serializable
data class BrandItem(
    @PrimaryKey()
    var name: String,
)