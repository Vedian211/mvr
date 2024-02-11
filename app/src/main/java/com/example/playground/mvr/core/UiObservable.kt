package com.example.playground.mvr.core

interface UiObservable<T: Any>: UiUpdate<T>, UpdateObserver<T> {

    fun clear()

    abstract class Base<T: Any>(private val empty: T): UiObservable<T> {

        private var observer: UiObserver<T> = UiObserver.Empty()
        protected var cache: T = empty

        override fun clear() {
            cache = empty
        }


        override fun updateObserver(uiObserver: UiObserver<T>) {
            observer = uiObserver
            observer.update(cache)
        }

        /**
         * Called by Model
         */
        override fun update(data: T) {
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