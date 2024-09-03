package com.example.tire.data.chassisNumber

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "ChassisNumber_list")
@Serializable
data class ChassisNumberItem(
    @PrimaryKey()
    var name: String,
)