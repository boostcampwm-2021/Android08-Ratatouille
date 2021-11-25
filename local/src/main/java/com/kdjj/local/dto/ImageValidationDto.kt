package com.kdjj.local.dto

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "UselessImage")
data class UselessImageDto(
    @PrimaryKey
    val imgPath: String,
)