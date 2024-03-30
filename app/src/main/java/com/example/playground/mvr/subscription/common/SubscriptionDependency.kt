package com.example.playground.mvr.subscription.common

import com.example.playground.mvr.core.ClearRepresentative
import com.example.playground.mvr.core.Core
import com.example.playground.mvr.core.Module
import com.example.playground.mvr.core.ProvideModule
import com.example.playground.mvr.core.Representative
import com.example.playground.mvr.subscription.progress.SubscriptionProgressModule
import com.example.playground.mvr.subscription.progress.presentation.SubscriptionProgressRepresentative
import com.example.playground.mvr.subscription.screen.SubscriptionModule
import com.example.playground.mvr.subscription.screen.presentation.SubscriptionRepresentative

class SubscriptionDependency(
    private val core: Core,
    private val clear: ClearRepresentative
): ProvideModule {

    private var scopeModule: SubscriptionScopeModule? = null
    private val provideScopeModule: () -> SubscriptionScopeModule = {
        if (scopeModule == null) scopeModule = SubscriptionScopeModule.Base()
        scopeModule!!
    }

    private val clearScopeModule: () -> Unit = {
        clear.clear(SubscriptionProgressRepresentative::class.java)
        clear.clear(SubscriptionRepresentative::class.java)
        scopeModule = null
    }

    override fun <T : Representative<*>> module(clazz: Class<T>) = when (clazz) {
        SubscriptionRepresentative::class.java -> SubscriptionModule(
            core = core,
            clear = clearScopeModule,
            provideScopeModule = provideScopeModule
        )

        SubscriptionProgressRepresentative::class.java -> SubscriptionProgressModule(
            core,
            provideScopeModule
        )

        else -> throw IllegalStateException("Unknown class $clazz")
    } as Module<T>
}