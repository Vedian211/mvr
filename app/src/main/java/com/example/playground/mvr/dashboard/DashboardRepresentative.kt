package com.example.playground.mvr.dashboard

import com.example.playground.mvr.core.ClearRepresentative
import com.example.playground.mvr.core.Representative
import com.example.playground.mvr.core.UiObserver
import com.example.playground.mvr.main.Navigation
import com.example.playground.mvr.main.Screen

interface DashboardRepresentative: Representative<PremiumDashboardUiState> {

    fun play()
    fun observed() = Unit

    class Premium(
        private val observable: PremiumDashboardObservable,
        private val clear: ClearRepresentative
    ): DashboardRepresentative {
        override fun play() {
            clear.clear(DashboardRepresentative::class.java)
            observable.update(PremiumDashboardUiState.Playing)
        }

        override fun startGettingUpdates(uiObserver: UiObserver<PremiumDashboardUiState>) {
            observable.updateObserver(uiObserver)
        }

        override fun stopGettingUpdates() {
            observable.updateObserver(EmptyDashboardObserver)
        }

        override fun observed() = observable.clear()
    }

    object EmptyDashboardObserver: DashboardObserver {
        override fun update(data: PremiumDashboardUiState) = Unit
    }

    class Base(private val navigation: Navigation.Update): DashboardRepresentative {
        override fun play() {
            navigation.update(Screen.Subscription)
        }
    }
}