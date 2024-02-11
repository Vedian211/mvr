package com.example.playground.mvr.dashboard

import com.example.playground.mvr.core.UiObservable

interface PremiumDashboardObservable: UiObservable<PremiumDashboardUiState> {
    class Base: UiObservable.Base<PremiumDashboardUiState>(PremiumDashboardUiState.Empty), PremiumDashboardObservable
}