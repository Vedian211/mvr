package com.example.playground.mvr.subscription.screen

import com.example.playground.mvr.core.Core
import com.example.playground.mvr.core.HandleDeath
import com.example.playground.mvr.core.Module
import com.example.playground.mvr.subscription.common.SubscriptionScopeModule
import com.example.playground.mvr.subscription.screen.presentation.SubscriptionRepresentative

class SubscriptionModule(
    private val core: Core,
    private val clear: () -> Unit,
    private val provideScopeModule: () -> SubscriptionScopeModule
): Module<SubscriptionRepresentative> {

    override fun representative(): SubscriptionRepresentative {
        return SubscriptionRepresentative.Base(
            clear = clear,
            navigation = core.navigation(),
            handleDeath = HandleDeath.Base(),
            observable = provideScopeModule.invoke().provideSubscriptionObservable()
        )
    }
}