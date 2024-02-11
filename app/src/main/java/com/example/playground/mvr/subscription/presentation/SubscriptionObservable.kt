package com.example.playground.mvr.subscription.presentation

import com.example.playground.mvr.core.UiObservable
import com.example.playground.mvr.subscription.SaveAndRestoreSubscriptionUiState

interface SubscriptionObservable: UiObservable<SubscriptionUiState>, SaveSubscriptionUiState {
    class Base: UiObservable.Base<SubscriptionUiState>(SubscriptionUiState.Empty),
        SubscriptionObservable {
        override fun saveState(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            saveState.save(cache)
        }
    }
}