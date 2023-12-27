package com.example.playground.mvr.subscription

import com.example.playground.mvr.core.ClearRepresentative
import com.example.playground.mvr.core.Representative
import com.example.playground.mvr.dashboard.DashboardRepresentative
import com.example.playground.mvr.main.Navigation
import com.example.playground.mvr.main.Screen
import com.example.playground.mvr.main.UserPremiumCache

interface SubscriptionRepresentative: Representative<Unit> {

    fun subscribe()

    class Base(
        private val save: UserPremiumCache.Save,
        private val navigation: Navigation.Update,
        private val clear: ClearRepresentative
    ): SubscriptionRepresentative {
        override fun subscribe() {
            save.saveUserPremium()
            clear.clear(DashboardRepresentative::class.java)
            clear.clear(SubscriptionRepresentative::class.java)
            navigation.update(Screen.Dashboard)
        }
    }
}