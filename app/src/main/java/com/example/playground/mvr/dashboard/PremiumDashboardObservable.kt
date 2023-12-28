package com.example.playground.mvr.dashboard

import com.example.playground.mvr.core.UiObservable
import com.example.playground.mvr.core.UiObserver

interface PremiumDashboardObservable: UiObservable<PremiumDashboardUiState> {
    class Base: UiObservable.Single<PremiumDashboardUiState>(PremiumDashboardUiState.Empty), PremiumDashboardObservable
}