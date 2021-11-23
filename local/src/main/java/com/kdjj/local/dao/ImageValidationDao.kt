package com.kdjj.local.dao

import androidx.room.*
import com.kdjj.local.dto.ImageValidationDto

@Dao
internal interface ImageValidationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertImageValidation(imageValidationDto: ImageValidationDto)

    @Query("UPDATE ImageValidation SET validate = :validate WHERE imgPath IN (:ids)")
    fun updateValidate(ids: List<String>, validate: Boolean)
}