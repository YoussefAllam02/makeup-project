package com.youssef.kotlincortines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    val stateflow = MutableStateFlow("Initial Value")

    launch {
        stateflow.collect { value ->
            println("Collected: $value")
        }
    }

    delay(1000)

    stateflow.value = "1"
    stateflow.value = "2"
}
