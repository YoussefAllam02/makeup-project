package com.example.implab.data.repo

import android.util.Log
import com.example.implab.data.local.LocalDataSource
import com.example.implab.data.models.Product
import com.example.implab.data.remote.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first

class RepoImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : Repo {

    override fun getAllProducts(): Flow<List<Product>> = localDataSource.getAllProducts()

    override fun getFavorites(): Flow<List<Product>> = localDataSource.getFavorites()

    override suspend fun refreshProducts() {
        try {
            Log.d("RepoImpl", "Starting refreshProducts()...")

            // Collect the flow and process the products
            remoteDataSource.getAllProducts().collect { remoteProducts ->
                Log.d("RepoImpl", "Fetched ${remoteProducts.size} products from API")

                localDataSource.deleteAllProducts()
                Log.d("RepoImpl", "Old products deleted.")

                remoteProducts.forEach { product ->
                    Log.d("RepoImpl", "Saving product: ${product.title}")
                    localDataSource.insertProduct(product)
                }

                Log.d("RepoImpl", "All products inserted!")
            }

        } catch (e: Exception) {
            Log.e("RepoImpl", "Refresh failed: ${e.message}")
        }
    }

    override suspend fun addToFav(product: Product) {
        localDataSource.updateProduct(product.copy(isFavorite = true))
    }

    override suspend fun removeFromFav(product: Product) {
        localDataSource.updateProduct(product.copy(isFavorite = false))
    }
}