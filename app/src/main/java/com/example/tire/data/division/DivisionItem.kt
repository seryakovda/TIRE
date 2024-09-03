package com.example.tire.data.division

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "Division_list")
@Serializable
data class DivisionItem(
    @PrimaryKey()
    var name: String,
)