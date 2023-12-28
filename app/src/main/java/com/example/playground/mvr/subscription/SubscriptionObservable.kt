package com.example.playground.mvr.subscription

import com.example.playground.mvr.core.UiObservable

interface SubscriptionObservable: UiObservable<SubscriptionUiState>, SaveSubscriptionUiState {
    class Base: UiObservable.Single<SubscriptionUiState>(SubscriptionUiState.Empty), SubscriptionObservable {
        override fun saveState(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            saveState.save(cache)
        }
    }
}