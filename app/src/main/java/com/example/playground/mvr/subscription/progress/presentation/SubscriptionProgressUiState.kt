package com.example.playground.mvr.subscription.progress.presentation

import com.example.playground.mvr.core.HideAndShow
import com.example.playground.mvr.core.UiUpdate
import com.example.playground.mvr.subscription.screen.presentation.SubscriptionInner
import java.io.Serializable

interface SubscriptionProgressUiState: Serializable, CanGoBack {

    override fun canGoBack() = true
    fun show(hideAndShow: HideAndShow)
    fun observed(representative: SubscriptionProgressRepresentative) = Unit
    fun restoreAfterDeath(
        innerSubscription: SubscriptionInner,
        observable: UiUpdate<SubscriptionProgressUiState>
    )

    object Show: SubscriptionProgressUiState {
        override fun canGoBack() = false
        override fun show(hideAndShow: HideAndShow) = hideAndShow.show()
        override fun restoreAfterDeath(
            innerSubscription: SubscriptionInner,
            observable: UiUpdate<SubscriptionProgressUiState>
        ) = innerSubscription.subscribeInner()
    }

    object Hide: SubscriptionProgressUiState {
        override fun show(hideAndShow: HideAndShow) = hideAndShow.hide()
        override fun observed(representative: SubscriptionProgressRepresentative) = representative.observed()
        override fun restoreAfterDeath(
            innerSubscription: SubscriptionInner,
            observable: UiUpdate<SubscriptionProgressUiState>
        ) = observable.update(this)
    }

    object Empty: SubscriptionProgressUiState {
        override fun show(hideAndShow: HideAndShow) = Unit
        override fun restoreAfterDeath(
            innerSubscription: SubscriptionInner,
            observable: UiUpdate<SubscriptionProgressUiState>
        ) = Unit
    }
}

interface CanGoBack {
    fun canGoBack(): Boolean
}