package com.example.playground.mvr.subscription

import com.example.playground.mvr.core.ClearRepresentative
import com.example.playground.mvr.core.Core
import com.example.playground.mvr.core.HandleDeath
import com.example.playground.mvr.core.Module
import com.example.playground.mvr.main.UserPremiumCache
import com.example.playground.mvr.subscription.data.BaseSubscriptionRepository
import com.example.playground.mvr.subscription.data.ForegroundServiceWrapper
import com.example.playground.mvr.subscription.data.SubscriptionCloudDataSource
import com.example.playground.mvr.subscription.domain.SubscriptionInteractor
import com.example.playground.mvr.subscription.presentation.SubscriptionObservable
import com.example.playground.mvr.subscription.presentation.SubscriptionRepresentative
import com.example.playground.mvr.subscription.presentation.SubscriptionUiMapper

class SubscriptionModule(private val core: Core, private val clear: ClearRepresentative): Module<SubscriptionRepresentative> {

    override fun representative(): SubscriptionRepresentative {
        val observable = SubscriptionObservable.Base()

        return SubscriptionRepresentative.Base(
            interactor = SubscriptionInteractor.Base(
                repository = BaseSubscriptionRepository(
                    userPremiumCache = UserPremiumCache.Base(core.sharedPreferences()),
                    subscriptionCloudDataSource = SubscriptionCloudDataSource.Base(),
                    foregroundServiceWrapper = ForegroundServiceWrapper.Base(core.provideWorkManager())
                )
            ),
            navigation = core.navigation(),
            clear = clear,
            handleDeath = HandleDeath.Base(),
            observable = observable,
            runAsync = core.runAsync(),
            mapper = SubscriptionUiMapper(observable = observable)
        )
    }
}