package com.youssef.kotlincortines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    val sharedFlow = MutableSharedFlow<Int>()

    launch {
        sharedFlow.collect { value ->
            println("Received: $value")
        }
    }

    println("Using emit:")
    launch {
        sharedFlow.emit(1)
        println("Emitted 1")
    }

    delay(100)

    println("\nUsing tryEmit:")
    val success = sharedFlow.tryEmit(2)
    println("tryEmit success: $success")
}
