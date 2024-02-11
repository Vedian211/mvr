package com.example.playground.mvr.subscription.presentation

import com.example.playground.mvr.core.HideAndShow
import java.io.Serializable

interface SubscriptionUiState: Serializable {

    fun observed(observer: SubscriptionObserved) = observer.observed()
    fun show(btnSubscribe: HideAndShow, pb: HideAndShow, btnFinish: HideAndShow)
    fun restoreAfterDeath(representative: SubscriptionInner, observable: SubscriptionObservable) = observable.update(this)

    object Initial: SubscriptionUiState {
        override fun show(btnSubscribe: HideAndShow, pb: HideAndShow, btnFinish: HideAndShow) {
            btnSubscribe.show()
            btnFinish.hide()
            pb.hide()
        }
    }
    object Loading: SubscriptionUiState {
        override fun show(btnSubscribe: HideAndShow, pb: HideAndShow, btnFinish: HideAndShow) {
            btnSubscribe.hide()
            btnFinish.hide()
            pb.show()
        }

        override fun restoreAfterDeath(
            representative: SubscriptionInner,
            observable: SubscriptionObservable
        ) {
            representative.subscribeInner()
        }

        override fun observed(observer: SubscriptionObserved) = Unit
    }
    object Success: SubscriptionUiState {
        override fun show(btnSubscribe: HideAndShow, pb: HideAndShow, btnFinish: HideAndShow) {
            btnSubscribe.hide()
            btnFinish.show()
            pb.hide()
        }
    }

    object Empty: SubscriptionUiState {
        override fun show(btnSubscribe: HideAndShow, pb: HideAndShow, btnFinish: HideAndShow) = Unit
        override fun restoreAfterDeath(representative: SubscriptionInner, observable: SubscriptionObservable) = Unit
    }
}