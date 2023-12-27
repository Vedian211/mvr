package com.example.playground.mvr.core

import androidx.annotation.MainThread

interface UiObservable<T: Any>: UiUpdate<T>, UpdateObserver<T> {

    abstract class Single<T: Any>: UiObservable<T> {

        @Volatile
        private var observer: UiObserver<T> = UiObserver.Empty()

        @Volatile
        private var cache: T? = null

        @MainThread
        override fun updateObserver(uiObserver: UiObserver<T>) = synchronized(this) {
            observer = uiObserver
            if (observer.isEmpty().not()) {
                cache?.let { observer.update(it) }
                cache = null
            }
        }

        /**
         * Called by Model
         */
        override fun update(data: T) = synchronized(this) {
            if (observer.isEmpty()) {
                cache = data
                // death process means cache = null
            } else {
                cache = null
                observer.update(data)
            }
        }
    }
}

interface UpdateObserver<T: Any> {
    fun updateObserver(uiObserver: UiObserver<T> = UiObserver.Empty())
}

interface UiUpdate<T: Any> {
    fun update(data: T)
}

interface UiObserver<T: Any>: UiUpdate<T> {

    fun isEmpty() = false

    class Empty<T: Any>: UiObserver<T> {
        override fun update(data: T) = Unit
        override fun isEmpty() = true
    }
}