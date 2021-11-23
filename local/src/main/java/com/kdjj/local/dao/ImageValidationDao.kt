package com.kdjj.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.kdjj.local.dto.ImageValidation

@Dao
internal interface ImageValidationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageValidation(imageValidation: ImageValidation)
}