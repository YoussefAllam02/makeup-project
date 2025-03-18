package com.example.implab.data.remote

import android.util.Log
import com.example.implab.data.models.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductsRemoteDataSource @Inject constructor(
    private val apiService: ProductsAPIService
) : RemoteDataSource {

    override suspend fun getAllProducts(): Flow<List<Product>> = flow {
        try {
            Log.d("ProductsRemoteDataSource", "Fetching products from API...")

            val response = apiService.getAllProducts()
            Log.d("ProductsRemoteDataSource", "Response Code: ${response.code()}")

            if (response.isSuccessful) {
                response.body()?.let { productResponse ->
                    Log.d("ProductsRemoteDataSource", "Fetched ${productResponse.products.size} products")
                    emit(productResponse.products) // Emit the list of products
                } ?: run {
                    Log.e("ProductsRemoteDataSource", "API returned empty response!")
                    emit(emptyList()) // Emit an empty list if the response is empty
                }
            } else {
                throw Exception("API Error: ${response.code()} ${response.message()}")
            }
        } catch (e: Exception) {
            Log.e("ProductsRemoteDataSource", "Network error: ${e.message}")
            emit(emptyList()) // Emit an empty list in case of an error
        }
    }
}