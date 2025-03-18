package com.example.implab.FavProducts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.implab.data.models.Product
import com.example.implab.data.repo.Repo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FavProductsViewModel(private val repo: Repo) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message

    init {
        getFavProducts()
    }

    private fun getFavProducts() {
        viewModelScope.launch {
            repo.getFavorites().collect { favorites ->
                _products.value = favorites
            }
        }
    }

    fun removeFromFavorites(product: Product) {
        viewModelScope.launch {
            try {
                repo.removeFromFav(product)
                _message.value = "${product.title} removed from favorites"
            } catch (e: Exception) {
                _message.value = "Error: ${e.message}"
            }
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}

class FavProductsFactory(private val repo: Repo) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavProductsViewModel(repo) as T
    }
}