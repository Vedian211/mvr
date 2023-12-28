package com.example.playground.mvr.subscription

import android.util.Log
import com.example.playground.mvr.core.ClearRepresentative
import com.example.playground.mvr.core.HandleDeath
import com.example.playground.mvr.core.Representative
import com.example.playground.mvr.core.UiObserver
import com.example.playground.mvr.dashboard.DashboardRepresentative
import com.example.playground.mvr.main.Navigation
import com.example.playground.mvr.main.Screen
import com.example.playground.mvr.main.UserPremiumCache

interface SubscriptionRepresentative: Representative<SubscriptionUiState>, SaveSubscriptionUiState, SubscriptionObserved, SubscriptionInner {

    fun subscribe()
    fun finish()
    fun init(restoreState: SaveAndRestoreSubscriptionUiState.Restore)

    class Base(
        private val handleDeath: HandleDeath,
        private val observable: SubscriptionObservable,
        private val save: UserPremiumCache.Save,
        private val navigation: Navigation.Update,
        private val clear: ClearRepresentative
    ): SubscriptionRepresentative {

        private fun createThread() = Thread {
            Thread.sleep(10_000)
            save.saveUserPremium()
            observable.update(SubscriptionUiState.Success)
        }

        override fun subscribe() {
            Log.d("MVR", "SubscriptionRepresentative subscribe: ")
            observable.update(SubscriptionUiState.Loading)
            subscribeInner()
        }

        override fun subscribeInner() {
            Log.d("MVR", "SubscriptionRepresentative subscribeInner: ")
            createThread().start()
        }

        override fun finish() {
            clear.clear(DashboardRepresentative::class.java)
            clear.clear(SubscriptionRepresentative::class.java)
            navigation.update(Screen.Dashboard)
        }

        override fun startGettingUpdates(uiObserver: UiObserver<SubscriptionUiState>) {
            observable.updateObserver(uiObserver)
        }

        override fun stopGettingUpdates() {
            observable.updateObserver()
        }

        override fun saveState(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            observable.saveState(saveState)
        }

        override fun observed() {
            observable.clear()
        }

        override fun init(restoreState: SaveAndRestoreSubscriptionUiState.Restore) {
            if (restoreState.isEmpty()) {
                // init local cache
                handleDeath.firstOpening()
                observable.update(SubscriptionUiState.Initial)
            } else {
                if (handleDeath.didDeathHappen()) {
                    // go to permanent storage and init localCache
                    handleDeath.deathHandled()
                    restoreState.restore().restoreAfterDeath(this, observable)
                    Log.d("MVR", "SubscriptionRepresentative restoreAfterDeath: ")

                } else {
                    // use local cache and don't use permanent
                }
            }
        }
    }
}

interface SaveSubscriptionUiState {
    fun saveState(saveState: SaveAndRestoreSubscriptionUiState.Save)
}

interface SubscriptionObserved {
    fun observed()
}

interface SubscriptionInner {
    fun subscribeInner()
}