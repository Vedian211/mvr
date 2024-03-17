package com.example.playground.mvr.subscription.presentation

import com.example.playground.mvr.core.UiUpdate
import com.example.playground.mvr.subscription.domain.SubscriptionResult

class SubscriptionUiMapper(private val observable: UiUpdate<SubscriptionUiState>): SubscriptionResult.Mapper {
    override fun mapSuccess(canGoBackCallback: (Boolean) -> Unit) {
        observable.update(SubscriptionUiState.Success)
        canGoBackCallback.invoke(true)
    }
}