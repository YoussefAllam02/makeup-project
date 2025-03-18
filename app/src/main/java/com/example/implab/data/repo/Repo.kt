package com.example.implab.data.repo

import com.example.implab.data.models.Product
import kotlinx.coroutines.flow.Flow

interface Repo {
    fun getAllProducts(): Flow<List<Product>>
    fun getFavorites(): Flow<List<Product>>
    suspend fun refreshProducts()
    suspend fun addToFav(product: Product)
    suspend fun removeFromFav(product: Product)
}