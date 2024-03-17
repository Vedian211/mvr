package com.example.playground.mvr.subscription.presentation

import com.example.playground.mvr.core.ClearRepresentative
import com.example.playground.mvr.core.HandleDeath
import com.example.playground.mvr.core.Representative
import com.example.playground.mvr.core.RunAsync
import com.example.playground.mvr.core.UiObserver
import com.example.playground.mvr.main.Navigation
import com.example.playground.mvr.main.Screen
import com.example.playground.mvr.subscription.SaveAndRestoreSubscriptionUiState
import com.example.playground.mvr.subscription.domain.SubscriptionInteractor
import com.example.playground.mvr.subscription.domain.SubscriptionResult

interface SubscriptionRepresentative: Representative<SubscriptionUiState>, SaveSubscriptionUiState,
    SubscriptionObserved, SubscriptionInner {

    fun subscribe()
    fun finish()
    fun comeback()
    suspend fun subscribeInternal()
    fun init(restoreState: SaveAndRestoreSubscriptionUiState.Restore)

    class Base(
        private val handleDeath: HandleDeath,
        private val observable: SubscriptionObservable,
        private val navigation: Navigation.Update,
        private val clear: ClearRepresentative,
        private val interactor: SubscriptionInteractor,
        private val mapper: SubscriptionResult.Mapper,
        runAsync: RunAsync
    ): SubscriptionRepresentative, Representative.Abstract<SubscriptionUiState>(runAsync = runAsync) {

        private var canGoBack = true
        private val uiBlock: (SubscriptionResult) -> Unit = { result -> result.map(mapper) { canGoBack = it } }

        override fun subscribe() {
            canGoBack = false
            observable.update(SubscriptionUiState.Loading)
            subscribeInner()
        }

        override fun subscribeInner() = handleAsync(
            backgroundBlock = { interactor.subscribe() },
            uiBlock = uiBlock
        )

        override suspend fun subscribeInternal() = handleAsyncInternal(
            backgroundBlock = { interactor.subscribeInternal() },
            uiBlock = uiBlock
        )

        override fun finish() {
            clear()
            clear.clear(SubscriptionRepresentative::class.java)
            navigation.update(Screen.Dashboard)
        }

        override fun comeback() {
            if (canGoBack) finish()
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