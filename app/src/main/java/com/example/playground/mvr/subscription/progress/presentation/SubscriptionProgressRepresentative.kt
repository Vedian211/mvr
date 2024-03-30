package com.example.playground.mvr.subscription.progress.presentation

import com.example.playground.mvr.core.HandleDeath
import com.example.playground.mvr.core.Representative
import com.example.playground.mvr.core.RunAsync
import com.example.playground.mvr.core.UiObserver
import com.example.playground.mvr.subscription.progress.domain.SubscriptionInteractor
import com.example.playground.mvr.subscription.progress.domain.SubscriptionResult
import com.example.playground.mvr.subscription.screen.presentation.Observed
import com.example.playground.mvr.subscription.screen.presentation.SubscriptionInner

interface SubscriptionProgressRepresentative: SubscriptionProgressRepresentativeActions {

    suspend fun subscribeInternal()
    fun restore(restoreState: SaveAndRestoreSubscriptionUiState.Restore)

    fun save(saveState: SaveAndRestoreSubscriptionUiState.Save)

    class Base(
        private val observable: SubscriptionProgressObservable,
        private val interactor: SubscriptionInteractor,
        private val mapper: SubscriptionResult.Mapper,
        private val handleDeath: HandleDeath,
        runAsync: RunAsync
    ): SubscriptionProgressRepresentative, Representative.Abstract<SubscriptionProgressUiState>(runAsync) {

        private val uiBlock: (SubscriptionResult) -> Unit = { it.map(mapper) }

        override fun init(firstRun: Boolean) {
            if (firstRun) {
                handleDeath.firstOpening()
                observable.update(SubscriptionProgressUiState.Hide)
            }
        }

        override fun restore(restoreState: SaveAndRestoreSubscriptionUiState.Restore) {
            if (handleDeath.didDeathHappen()) {
                handleDeath.deathHandled()
                restoreState.restore().restoreAfterDeath(this, observable)
            }
        }

        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            observable.save(saveState)
        }

        override fun observed() {
            observable.clear()
        }

        override fun startGettingUpdates(uiObserver: UiObserver<SubscriptionProgressUiState>) {
            observable.updateObserver(uiObserver)
        }

        override fun stopGettingUpdates() {
            observable.updateObserver()
        }

        override fun comeback(data: ComeBack<Boolean>) {
            data.comeback(observable.canGoBack())
        }

        override fun subscribe() {
            observable.update(SubscriptionProgressUiState.Show)
            subscribeInner()
        }

        override fun subscribeInner() {
            handleAsync(
                backgroundBlock = { interactor.subscribe() },
                uiBlock = uiBlock
            )
        }

        override suspend fun subscribeInternal() {
            handleAsync(
                backgroundBlock = { interactor.subscribeInternal() },
                uiBlock = uiBlock
            )
        }
    }
}
interface SubscriptionProgressRepresentativeActions:
    Representative<SubscriptionProgressUiState>,
    SubscriptionInner,
    ComeBack<ComeBack<Boolean>>,
    Init,
    Observed,
    Subscribe