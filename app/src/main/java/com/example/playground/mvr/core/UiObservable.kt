package com.example.playground.mvr.core

import androidx.annotation.MainThread

interface UiObservable<T: Any>: UiUpdate<T>, UpdateObserver<T> {

    fun clear()

    abstract class Single<T: Any>(private val empty: T): UiObservable<T> {

        @Volatile
        private var observer: UiObserver<T> = UiObserver.Empty()

        @Volatile
        protected var cache: T = empty

        override fun clear() {
            cache = empty
        }


        @MainThread
        override fun updateObserver(uiObserver: UiObserver<T>) = synchronized(this) {
            observer = uiObserver
            observer.update(cache)
        }

        /**
         * Called by Model
         */
        override fun update(data: T) = synchronized(this) {
            cache = data
            observer.update(cache)
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
    class Empty<T: Any>: UiObserver<T> {
        override fun update(data: T) = Unit
    }
}