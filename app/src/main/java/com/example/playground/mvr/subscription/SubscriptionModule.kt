package com.example.playground.mvr.subscription

import com.example.playground.mvr.core.ClearRepresentative
import com.example.playground.mvr.core.Core
import com.example.playground.mvr.core.HandleDeath
import com.example.playground.mvr.core.Module
import com.example.playground.mvr.main.UserPremiumCache

class SubscriptionModule(private val core: Core, private val clear: ClearRepresentative): Module<SubscriptionRepresentative> {

    override fun representative(): SubscriptionRepresentative {
        return SubscriptionRepresentative.Base(
            save = UserPremiumCache.Base(core.sharedPreferences()),
            navigation = core.navigation(),
            clear = clear,
            handleDeath = HandleDeath.Base(),
            observable = SubscriptionObservable.Base()
        )
    }
}