package com.example.playground.mvr.subscription

import android.os.Bundle
import com.example.playground.mvr.core.SaveAndRestoreState

interface SaveAndRestoreSubscriptionUiState {

    interface Save: SaveAndRestoreState.Save<SubscriptionUiState>

    interface Restore: SaveAndRestoreState.Restore<SubscriptionUiState>

    interface Mutable: Save, Restore

    class Base(bundle: Bundle?): SaveAndRestoreState.Abstract<SubscriptionUiState>(
        bundle = bundle,
        key = KEY,
        clazz = SubscriptionUiState::class.java
    ), Mutable
}

private const val KEY = "SubscriptionUiStateKey"