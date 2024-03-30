package com.example.playground.mvr.subscription.progress.presentation

import com.example.playground.mvr.core.UiObservable

interface SubscriptionProgressObservable : UiObservable<SubscriptionProgressUiState>, CanGoBack {

    fun save(saveState: SaveAndRestoreSubscriptionUiState.Save)

    class Base : UiObservable.Base<SubscriptionProgressUiState>(SubscriptionProgressUiState.Empty), SubscriptionProgressObservable {

        override fun canGoBack() = cache.canGoBack()

        override fun save(saveState: SaveAndRestoreSubscriptionUiState.Save) = saveState.save(cache)
    }
}