package com.youssef.kotlincortines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

suspend fun main():Unit= coroutineScope {
    val sharedFlow = MutableSharedFlow<Int>()

    launch {
        sharedFlow.collect{
            println("Collected: $it")

        }
    }
    launch {
        sharedFlow.collect{
            println("second collector: $it")
        }
    }
    sharedFlow.emit(5)
    sharedFlow.emit(10)
println("done")
}