package com.example.playground.mvr

import android.app.Application

class App: Application() {

    lateinit var mainRepresentative: MainRepresentative

    override fun onCreate() {
        super.onCreate()

        mainRepresentative = MainRepresentative.Base(UiObservable.Single())
    }
}