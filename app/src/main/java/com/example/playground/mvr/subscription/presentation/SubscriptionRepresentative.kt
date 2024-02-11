package com.example.playground.mvr.subscription.presentation

import com.example.playground.mvr.core.ClearRepresentative
import com.example.playground.mvr.core.HandleDeath
import com.example.playground.mvr.core.Representative
import com.example.playground.mvr.core.UiObserver
import com.example.playground.mvr.main.Navigation
import com.example.playground.mvr.main.Screen
import com.example.playground.mvr.subscription.SaveAndRestoreSubscriptionUiState
import com.example.playground.mvr.subscription.domain.SubscriptionInteractor

interface SubscriptionRepresentative: Representative<SubscriptionUiState>, SaveSubscriptionUiState,
    SubscriptionObserved, SubscriptionInner {

    fun subscribe()
    fun finish()
    fun init(restoreState: SaveAndRestoreSubscriptionUiState.Restore)

    class Base(
        private val handleDeath: HandleDeath,
        private val observable: SubscriptionObservable,
        private val navigation: Navigation.Update,
        private val clear: ClearRepresentative,
        private val interactor: SubscriptionInteractor
    ): SubscriptionRepresentative {

        override fun subscribe() {
            observable.update(SubscriptionUiState.Loading)
            subscribeInner()
        }

        override fun subscribeInner() {
            interactor.subscribe { observable.update(SubscriptionUiState.Success) }
        }

        override fun finish() {
            clear.clear(SubscriptionRepresentative::class.java)
            navigation.update(Screen.Dashboard)
        }

        override fun startGettingUpdates(uiObserver: UiObserver<SubscriptionUiState>) {
            observable.updateObserver(uiObserver)
        }

        override fun stopGettingUpdates() {
            observable.updateObserver(EmptySubscriptionObserver)
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
                } else {
                    // use local cache and don't use permanent
                }
            }
        }
    }
}

object EmptySubscriptionObserver: SubscriptionObserver {
    override fun update(data: SubscriptionUiState) = Unit
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