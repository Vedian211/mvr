package com.example.playground.mvr.dashboard

import com.example.playground.mvr.core.Module
import com.example.playground.mvr.core.Core
import com.example.playground.mvr.main.UserPremiumCache

class DashboardModule(private val core: Core): Module<DashboardRepresentative> {
    override fun representative(): DashboardRepresentative {
        val isUserPremium = UserPremiumCache.Base(core.sharedPreferences()).isUserPremium()

        return if (isUserPremium) DashboardRepresentative.Premium(PremiumDashboardObservable.Base())
        else DashboardRepresentative.Base(core.navigation())
    }
}