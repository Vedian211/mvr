package com.example.playground.mvr.dashboard

import com.example.playground.mvr.core.ClearRepresentative
import com.example.playground.mvr.core.Module
import com.example.playground.mvr.core.Core
import com.example.playground.mvr.main.UserPremiumCache

class DashboardModule(
    private val core: Core,
    private val clearRepresentative: ClearRepresentative
): Module<DashboardRepresentative> {
    override fun representative(): DashboardRepresentative {
        val isUserPremium = UserPremiumCache.Base(core.sharedPreferences()).isUserPremium()

        return if (isUserPremium) DashboardRepresentative.Premium(
            observable = PremiumDashboardObservable.Base(),
            clear = clearRepresentative
        )
        else DashboardRepresentative.Base(core.navigation())
    }
}