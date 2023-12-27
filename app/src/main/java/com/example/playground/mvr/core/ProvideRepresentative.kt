package com.example.playground.mvr.core

import com.example.playground.mvr.dashboard.DashboardModule
import com.example.playground.mvr.dashboard.DashboardRepresentative
import com.example.playground.mvr.main.MainModule
import com.example.playground.mvr.main.MainRepresentative
import com.example.playground.mvr.subscription.SubscriptionModule
import com.example.playground.mvr.subscription.SubscriptionRepresentative

interface ProvideRepresentative {

    fun <T: Representative<*>> provideRepresentative(clazz: Class<T>): T

    class Factory(private val core: Core, private val clear: ClearRepresentative): ProvideRepresentative {
        @Suppress("UNCHECKED_CAST")
        override fun <T : Representative<*>> provideRepresentative(clazz: Class<T>): T {
            return when (clazz) {
                MainRepresentative::class.java -> MainModule(core).representative()
                DashboardRepresentative::class.java -> DashboardModule(core).representative()
                SubscriptionRepresentative::class.java -> SubscriptionModule(core, clear).representative()
                else -> throw Exception("Unknown class $clazz")
            } as T
        }
    }
}