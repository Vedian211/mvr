package com.example.playground.mvr.subscription.common

import com.example.playground.mvr.subscription.screen.presentation.SubscriptionObservable

interface SubscriptionScopeModule {
    fun provideSubscriptionObservable(): SubscriptionObservable

    class Base : SubscriptionScopeModule {

        private val observable = SubscriptionObservable.Base()

        override fun provideSubscriptionObservable() = observable
    }
}