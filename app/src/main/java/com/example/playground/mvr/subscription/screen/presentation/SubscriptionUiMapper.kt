package com.example.playground.mvr.subscription.screen.presentation

import com.example.playground.mvr.core.UiUpdate
import com.example.playground.mvr.subscription.progress.domain.SubscriptionResult
import com.example.playground.mvr.subscription.progress.presentation.SubscriptionProgressUiState

class SubscriptionUiMapper(
    private val observable: UiUpdate<SubscriptionUiState>,
    private val progressObservable: UiUpdate<SubscriptionProgressUiState>
) : SubscriptionResult.Mapper {

    override fun mapSuccess() {
        observable.update(SubscriptionUiState.Success)
        progressObservable.update(SubscriptionProgressUiState.Hide)
    }
}