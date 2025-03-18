package com.example.implab.data.local
//////////
import com.example.implab.data.models.Product
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    fun getAllProducts(): Flow<List<Product>>
    fun getFavorites(): Flow<List<Product>>
    suspend fun insertProduct(product: Product)
    suspend fun updateProduct(product: Product)
    suspend fun deleteAllProducts()
}