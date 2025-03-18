package com.example.implab.data.local

import android.util.Log
import com.example.implab.data.models.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProductsLocalDataSource(
    private val productDao: ProductDao
) : LocalDataSource {

    override fun getAllProducts() = productDao.getAllProducts()
    override fun getFavorites() = productDao.getFavorites()

    override suspend fun insertProduct(product: Product) =
        withContext(Dispatchers.IO) {
            Log.d("ProductsLocalDataSource", "Inserting product: ${product.title}") // ✅
            productDao.insertProduct(product)
        }


    override suspend fun updateProduct(product: Product) =
        withContext(Dispatchers.IO) {
            productDao.updateProduct(product)
        }

    override suspend fun deleteAllProducts() =
        withContext(Dispatchers.IO) {
            productDao.deleteAllProducts()
        }
}