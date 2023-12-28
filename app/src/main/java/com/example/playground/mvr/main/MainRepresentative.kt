package com.example.playground.mvr.main

import com.example.playground.mvr.core.Representative
import com.example.playground.mvr.core.UiObserver

interface MainRepresentative: Representative<Screen> {

    fun showDashboard(firstTime: Boolean)

    class Base(private val navigation: Navigation.Mutable): MainRepresentative {
        override fun showDashboard(firstTime: Boolean) {
            if (firstTime) {
                navigation.update(Screen.Dashboard)
            }
        }

        override fun startGettingUpdates(uiObserver: UiObserver<Screen>) {
            navigation.updateObserver(uiObserver)
        }

        override fun stopGettingUpdates() {
            navigation.updateObserver()
        }
    }
}