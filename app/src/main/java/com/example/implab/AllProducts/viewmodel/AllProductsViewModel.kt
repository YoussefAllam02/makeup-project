package com.example.implab.AllProducts.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.implab.data.models.Product
import com.example.implab.data.repo.Repo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AllProductsViewModel(private val repo: Repo) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        Log.d("AllProductsViewModel", "ViewModel initialized")
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            Log.d("AllProductsViewModel", "Calling refreshProducts()...") // ✅
            refreshProducts()

            repo.getAllProducts().collectLatest { products ->
                _products.value = products
                Log.d("AllProductsViewModel", "Products loaded from DB: ${products.size}") // ✅
            }
        }
    }

    fun refreshProducts() {
        viewModelScope.launch {
            _loading.value = true
            try {
                repo.refreshProducts()
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _loading.value = false
            }
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            try {
                if (product.isFavorite) {
                    repo.removeFromFav(product)
                } else {
                    repo.addToFav(product)
                }
            } catch (e: Exception) {
                _error.value = "Failed to update favorite: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}

class AllProductsFactory(private val repo: Repo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AllProductsViewModel(repo) as T
    }
}