package com.example.playground.mvr.dashboard

import com.example.playground.mvr.core.Representative
import com.example.playground.mvr.core.UiObserver
import com.example.playground.mvr.main.Navigation
import com.example.playground.mvr.main.Screen

interface DashboardRepresentative: Representative<PremiumDashboardUiState> {

    fun play()

    class Premium(private val observable: PremiumDashboardObservable): DashboardRepresentative {
        override fun play() {
            observable.update(PremiumDashboardUiState.Playing)
        }

        override fun startGettingUpdates(uiObserver: UiObserver<PremiumDashboardUiState>) {
            observable.updateObserver(uiObserver)
        }

        override fun stopGettingUpdates() {
            observable.updateObserver()
        }
    }

    class Base(private val navigation: Navigation.Update): DashboardRepresentative {
        override fun play() {
            navigation.update(Screen.Subscription)
        }
    }
}