package com.example.implab.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "products_table")
data class Product(
    @PrimaryKey
    val id: Int,
    val title: String,
    val price: Double,
    val thumbnail: String,
    val rating: Double,
    var isFavorite: Boolean = false // Not part of API response
)