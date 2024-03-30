package com.example.playground.mvr.subscription.progress

import com.example.playground.mvr.core.Core
import com.example.playground.mvr.core.HandleDeath
import com.example.playground.mvr.core.Module
import com.example.playground.mvr.main.UserPremiumCache
import com.example.playground.mvr.subscription.common.SubscriptionScopeModule
import com.example.playground.mvr.subscription.progress.data.BaseSubscriptionRepository
import com.example.playground.mvr.subscription.progress.data.ForegroundServiceWrapper
import com.example.playground.mvr.subscription.progress.data.SubscriptionCloudDataSource
import com.example.playground.mvr.subscription.progress.domain.SubscriptionInteractor
import com.example.playground.mvr.subscription.progress.presentation.SubscriptionProgressObservable
import com.example.playground.mvr.subscription.progress.presentation.SubscriptionProgressRepresentative
import com.example.playground.mvr.subscription.screen.presentation.SubscriptionUiMapper

class SubscriptionProgressModule(
    private val core: Core,
    private val provideScopeModule: () -> SubscriptionScopeModule
) : Module<SubscriptionProgressRepresentative> {

    override fun representative(): SubscriptionProgressRepresentative {
        val observable = SubscriptionProgressObservable.Base()
        val interactor = SubscriptionInteractor.Base(
            repository = BaseSubscriptionRepository(
                foregroundServiceWrapper = ForegroundServiceWrapper.Base(workManager = core.provideWorkManager()),
                subscriptionCloudDataSource = SubscriptionCloudDataSource.Base(),
                userPremiumCache = UserPremiumCache.Base(sharedPreferences = core.sharedPreferences())
            )
        )
        val mapper = SubscriptionUiMapper(
            provideScopeModule.invoke().provideSubscriptionObservable(),
            observable
        )

        return SubscriptionProgressRepresentative.Base(
            observable = observable,
            handleDeath = HandleDeath.Base(),
            interactor = interactor,
            mapper = mapper,
            runAsync = core.runAsync()
        )
    }
}