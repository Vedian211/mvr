package com.example.playground.mvr.subscription.screen.presentation

import com.example.playground.mvr.core.HideAndShow
import com.example.playground.mvr.subscription.progress.presentation.Subscribe
import java.io.Serializable

interface SubscriptionUiState: Serializable {

    fun observed(observer: Observed) = observer.observed()
    fun show(btnSubscribe: HideAndShow, pbSubscribe: Subscribe, btnFinish: HideAndShow)
    fun restoreAfterDeath(observable: SubscriptionObservable) = observable.update(this)

    object Initial: SubscriptionUiState {
        override fun show(btnSubscribe: HideAndShow, pbSubscribe: Subscribe, btnFinish: HideAndShow) {
            btnSubscribe.show()
            btnFinish.hide()
        }
    }
    object Loading: SubscriptionUiState {
        override fun show(btnSubscribe: HideAndShow, pbSubscribe: Subscribe, btnFinish: HideAndShow) {
            pbSubscribe.subscribe()
            btnSubscribe.hide()
            btnFinish.hide()
        }
    }
    object Success: SubscriptionUiState {
        override fun show(btnSubscribe: HideAndShow, pbSubscribe: Subscribe, btnFinish: HideAndShow) {
            btnSubscribe.hide()
            btnFinish.show()
        }
    }

    object Empty: SubscriptionUiState {
        override fun show(btnSubscribe: HideAndShow, pbSubscribe: Subscribe, btnFinish: HideAndShow) = Unit
        override fun restoreAfterDeath(observable: SubscriptionObservable) = Unit
        override fun observed(observer: Observed) = Unit
    }
}