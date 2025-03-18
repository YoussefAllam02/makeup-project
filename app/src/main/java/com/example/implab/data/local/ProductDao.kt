package com.example.implab.data.local

import androidx.room.*
import com.example.implab.data.models.Product
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Update
    suspend fun updateProduct(product: Product)

    @Query("DELETE FROM products_table")
    suspend fun deleteAllProducts()

    @Query("SELECT * FROM products_table")
    fun getAllProducts(): Flow<List<Product>>

    @Query("SELECT * FROM products_table WHERE isFavorite = 1")
    fun getFavorites(): Flow<List<Product>>
}