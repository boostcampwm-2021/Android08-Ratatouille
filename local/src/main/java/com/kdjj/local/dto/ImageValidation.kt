package com.kdjj.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ImageValidation")
data class ImageValidation(
    @PrimaryKey
    val imgPath: String,
    val validate: Boolean
)