package com.youssef.kotlincortines


import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking

fun evenNumbersFlow() = flow {
    var number = 0
    repeat(20) {
        emit(number)
        number += 2
        delay(1000L)
    }
}

fun main() = runBlocking {
    evenNumbersFlow()
        .take(10) 
        .collect { println("Collected: $it") }
}
