package com.example.implab.AllProducts.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.implab.AllProducts.viewmodel.AllProductsFactory
import com.example.implab.AllProducts.viewmodel.AllProductsViewModel
import com.example.implab.data.local.ProductsDataBase
import com.example.implab.data.local.ProductsLocalDataSource
import com.example.implab.data.models.Product
import com.example.implab.data.remote.ProductsRemoteDataSource
import com.example.implab.data.remote.RetrofitHelper
import com.example.implab.data.repo.RepoImpl
import com.example.implab.ui.theme.ImpLabTheme
import kotlinx.coroutines.launch

class AllProductsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImpLabTheme {
                val viewModel: AllProductsViewModel = viewModel(
                    factory = AllProductsFactory(
                        RepoImpl(
                            ProductsLocalDataSource(
                                ProductsDataBase.getDatabase(this).productDao()
                            ),
                            ProductsRemoteDataSource(RetrofitHelper.apiService)
                        )
                    )
                )
                AllProductsUI(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AllProductsUI(viewModel: AllProductsViewModel) {
    val products by viewModel.products.collectAsState()
    val error by viewModel.error.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(error) {
        error?.let {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Short
                )
                viewModel.clearError()
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.refreshProducts() },
                icon = { Icon(Icons.Default.Refresh, "Refresh") },
                text = { Text("Refresh Products") }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            LazyColumn(modifier = Modifier.padding(innerPadding)) {
                items(products.size) { index ->
                    val product = products[index]
                    ProductRow(
                        product = product,
                        actionName = if (product.isFavorite) "★ Remove Favorite" else "☆ Add Favorite",
                        onAction = { viewModel.toggleFavorite(product) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductRow(
    product: Product,
    actionName: String,
    onAction: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(10.dp)
        ) {
            GlideImage(
                modifier = Modifier.size(100.dp),
                model = product.thumbnail,
                contentDescription = "Product Image"
            )

            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Price: ${product.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Rating: ${product.rating} ⭐",
                    style = MaterialTheme.typography.bodyMedium
                )
                Button(
                    onClick = onAction,
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(actionName, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
}