package com.example.playground.mvr.subscription

import com.example.playground.mvr.core.ClearRepresentative
import com.example.playground.mvr.core.Core
import com.example.playground.mvr.core.HandleDeath
import com.example.playground.mvr.core.Module
import com.example.playground.mvr.main.UserPremiumCache
import com.example.playground.mvr.subscription.data.BaseSubscriptionRepository
import com.example.playground.mvr.subscription.data.SubscriptionCloudDataSource
import com.example.playground.mvr.subscription.domain.SubscriptionInteractor
import com.example.playground.mvr.subscription.presentation.SubscriptionObservable
import com.example.playground.mvr.subscription.presentation.SubscriptionRepresentative

class SubscriptionModule(private val core: Core, private val clear: ClearRepresentative): Module<SubscriptionRepresentative> {

    override fun representative(): SubscriptionRepresentative {
        return SubscriptionRepresentative.Base(
            interactor = SubscriptionInteractor.Base(
                repository = BaseSubscriptionRepository(
                    userPremiumCache = UserPremiumCache.Base(core.sharedPreferences()),
                    subscriptionCloudDataSource = SubscriptionCloudDataSource.Base()
                )
            ),
            navigation = core.navigation(),
            clear = clear,
            handleDeath = HandleDeath.Base(),
            observable = SubscriptionObservable.Base(),
            runAsync = core.runAsync()
        )
    }
}