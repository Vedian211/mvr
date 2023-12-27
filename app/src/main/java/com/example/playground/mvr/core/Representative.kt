package com.example.playground.mvr.core

interface Representative<T: Any> {
    fun startGettingUpdates(uiObserver: UiObserver<T>) = Unit
    fun stopGettingUpdates() = Unit
}