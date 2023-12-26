package com.example.playground.mvr

import com.example.playground.R

interface MainRepresentative {

    fun startAsync()
    fun startGettingUpdates(uiObserver: UiObserver<Int>)
    fun stopGettingUpdates()

    class Base(private val observable: UiObservable<Int>): MainRepresentative {

        private val thread = Thread {
            Thread.sleep(3000) // any sync work
            observable.update(R.string.hello)
        }

        override fun startAsync() {
            thread.start()
        }

        override fun startGettingUpdates(uiObserver: UiObserver<Int>) {
            observable.updateObserver(uiObserver)
        }

        override fun stopGettingUpdates() {
            observable.updateObserver()
        }
    }
}