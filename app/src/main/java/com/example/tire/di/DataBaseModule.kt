package com.example.tire.di

import com.example.tire.data.DB
import com.example.tire.data.brand.BrandDao
import com.example.tire.data.chassisNumber.ChassisNumberDao
import com.example.tire.data.division.DivisionDao
import com.example.tire.data.filesToUpload.FilesToUploadDao
import com.example.tire.data.modelCar.ModelDao
import com.example.tire.data.standard_size.StandardSizeDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    fun provideBrandDao(database: DB): BrandDao {
        return database.brandDao()
    }

    @Provides
    fun provideStandardSizeDao(database: DB): StandardSizeDao {
        return database.standardSizeDao()
    }

    @Provides
    fun provideModelDao(database: DB): ModelDao {
        return database.modelDao()
    }

    @Provides
    fun provideDivisionDao(database: DB): DivisionDao {
        return database.divisionDao()
    }
    @Provides
    fun chassisNumberDao(database: DB): ChassisNumberDao {
        return database.chassisNumberDao()
    }
    @Provides
    fun filesToUploadDao(database: DB): FilesToUploadDao {
        return database.filesToUploadDao()
    }
}