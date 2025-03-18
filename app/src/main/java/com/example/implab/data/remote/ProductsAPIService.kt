// ProductsAPIService.kt (direct list response)
package com.example.implab.data.remote

import com.example.implab.data.models.Product
import retrofit2.Response
import retrofit2.http.GET

interface ProductsAPIService {
    @GET("products")
    suspend fun getAllProducts(): Response<ProductResponse>

}