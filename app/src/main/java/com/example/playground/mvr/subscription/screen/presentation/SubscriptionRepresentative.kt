package com.example.playground.mvr.subscription.screen.presentation

import com.example.playground.mvr.core.HandleDeath
import com.example.playground.mvr.core.Representative
import com.example.playground.mvr.core.UiObserver
import com.example.playground.mvr.main.Navigation
import com.example.playground.mvr.main.Screen
import com.example.playground.mvr.subscription.progress.presentation.ComeBack
import com.example.playground.mvr.subscription.progress.presentation.Subscribe

interface SubscriptionRepresentative: SubscriptionRepresentativeActions {

    fun finish()
    fun init(restoreState: SaveAndRestoreSubscriptionUiState.Restore)

    class Base(
        private val clear: () -> Unit,
        private val handleDeath: HandleDeath,
        private val navigation: Navigation.Update,
        private val observable: SubscriptionObservable
    ): SubscriptionRepresentative {

        override fun finish() {
            navigation.update(Screen.Dashboard)
            clear.invoke()
        }

        override fun comeback(data: Boolean) {
            if (data) finish()
        }

        override fun saveState(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            observable.saveState(saveState)
        }

        override fun observed() {
            observable.clear()
        }

        override fun subscribe() {
            observable.update(SubscriptionUiState.Loading)
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
                    restoreState.restore().restoreAfterDeath(observable)
                }
            }
        }

        override fun startGettingUpdates(uiObserver: UiObserver<SubscriptionUiState>) {
            observable.updateObserver(uiObserver)
        }

        override fun stopGettingUpdates() {
            observable.updateObserver(EmptySubscriptionObserver)
        }
    }
}

object EmptySubscriptionObserver: SubscriptionObserver {
    override fun update(data: SubscriptionUiState) = Unit
}

interface SaveSubscriptionUiState {
    fun saveState(saveState: SaveAndRestoreSubscriptionUiState.Save)
}

interface Observed {
    fun observed()
}

interface SubscriptionInner {
    fun subscribeInner()
}

interface SubscriptionRepresentativeActions: Representative<SubscriptionUiState>, SaveSubscriptionUiState, Observed, Subscribe, ComeBack<Boolean>