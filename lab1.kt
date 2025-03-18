package com.youssef.kotlincoroutines

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking

fun getNumbers() = flow {
    for (i in 1..10) {
        emit("the number is $i")
    }
}

suspend fun main() = coroutineScope {
    getNumbers()
        .filter { item -> item.filterNot { it.isLetter() }.trim().toInt() > 5 }
        .collect { println(it) }
}
