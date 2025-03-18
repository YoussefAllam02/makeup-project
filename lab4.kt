package com.youssef.kotlincortines

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    val flow = flow {
        for (i in 1..5) {
            emit(i)
            delay(100)
        }
    }

    println("Using collect:")
    flow.collect { value ->
        delay(300)
        println("Collected $value")
    }

    println("\nUsing collectLatest:")
    flow.collectLatest { value ->
        delay(300)
        println("Collected $value")
    }
}
