package com.youssef.kotlincortines
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchScreen()
        }
    }
}

@Composable
fun SearchScreen() {
    val searchFlow = remember { MutableSharedFlow<String>(replay = 1) }
    var searchText by remember { mutableStateOf("") }
    val namesList = listOf("Ali", "Ayman", "Omar", "ahmed", "Aya", "Osman", "Amira", "Nada","youssef," ,
            "alyaa","hager","abram","habiba","jilan","nour")

    val coroutineScope = rememberCoroutineScope()

    var filteredNames by remember { mutableStateOf(emptyList<String>()) }

    LaunchedEffect(searchFlow) {
        searchFlow.collectLatest { query ->
            filteredNames = namesList.filter { it.startsWith(query, ignoreCase = true) }
        }
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        BasicTextField(
            value = searchText,
            onValueChange = { newText ->
                searchText = newText
                coroutineScope.launch {
                    searchFlow.emit(newText)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    if (searchText.isEmpty()) Text("Searchasigment")
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(filteredNames.size) { index ->
                Text(
                    text = filteredNames[index],
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSearchScreen() {
    SearchScreen()
}
