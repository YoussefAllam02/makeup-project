package com.example.implab.FavProducts.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.implab.AllProducts.view.ProductRow
import com.example.implab.FavProducts.viewmodel.FavProductsFactory
import com.example.implab.FavProducts.viewmodel.FavProductsViewModel
import com.example.implab.data.local.ProductsDataBase
import com.example.implab.data.local.ProductsLocalDataSource
import com.example.implab.data.remote.ProductsRemoteDataSource
import com.example.implab.data.remote.RetrofitHelper
import com.example.implab.data.repo.RepoImpl
import com.example.implab.ui.theme.ImpLabTheme
import kotlinx.coroutines.launch

class FavActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImpLabTheme {
                val viewModel: FavProductsViewModel = viewModel(
                    factory = FavProductsFactory(
                        RepoImpl(
                            ProductsLocalDataSource(
                                ProductsDataBase.getDatabase(this).productDao()
                            ),
                            ProductsRemoteDataSource(RetrofitHelper.apiService)
                        )
                    )
                )
                AllFavProductsUI(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun AllFavProductsUI(viewModel: FavProductsViewModel) {
    val products by viewModel.products.collectAsState()
    val message by viewModel.message.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(message) {
        message?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
                viewModel.clearMessage()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(products.size) { index ->
                ProductRow(
                    product = products[index],
                    actionName = "Remove from Favorites",
                    onAction = { viewModel.removeFromFavorites(products[index]) }
                )
            }
        }
    }
}