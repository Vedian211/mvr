package com.example.playground.mvr.dashboard

import com.example.playground.mvr.core.HideAndShow

interface PremiumDashboardUiState {

    fun show(button: HideAndShow, text: HideAndShow)

    object Playing: PremiumDashboardUiState {
        override fun show(button: HideAndShow, text: HideAndShow) {
            button.hide()
            text.show()
        }
    }
}