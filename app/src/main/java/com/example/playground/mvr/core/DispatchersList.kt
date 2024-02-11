package com.example.playground.mvr.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatchersList {

    fun background(): CoroutineDispatcher
    fun ui(): CoroutineDispatcher

    class Base: DispatchersList {
        override fun background() = Dispatchers.IO
        override fun ui() = Dispatchers.Main
    }
}