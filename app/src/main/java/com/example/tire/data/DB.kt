package com.example.tire.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.example.tire.data.brand.BrandDao
import com.example.tire.data.brand.BrandItem
import com.example.tire.data.chassisNumber.ChassisNumberDao
import com.example.tire.data.chassisNumber.ChassisNumberItem
import com.example.tire.data.division.DivisionDao
import com.example.tire.data.division.DivisionItem
import com.example.tire.data.filesToUpload.FilesToUploadDao
import com.example.tire.data.filesToUpload.FilesToUploadItem
import com.example.tire.data.modelCar.ModelDao
import com.example.tire.data.modelCar.ModelItem

import com.example.tire.data.standard_size.StandardSizeDao
import com.example.tire.data.standard_size.StandardSizeItem

@Database(entities = [
    StandardSizeItem::class,
    BrandItem::class,
    ModelItem::class,
    DivisionItem::class,
    ChassisNumberItem::class,
    FilesToUploadItem::class
                     ], version = 2,exportSchema = true)
abstract class DB : RoomDatabase() {
    abstract fun brandDao():            BrandDao
    abstract fun standardSizeDao():     StandardSizeDao
    abstract fun modelDao():            ModelDao
    abstract fun divisionDao():         DivisionDao
    abstract fun chassisNumberDao():    ChassisNumberDao
    abstract fun filesToUploadDao():    FilesToUploadDao
}