package com.example.playground.mvr.main

import com.example.playground.mvr.core.UiObservable
import com.example.playground.mvr.core.UiUpdate
import com.example.playground.mvr.core.UpdateObserver

interface Navigation {
    interface Update: UiUpdate<Screen>
    interface Observe: UpdateObserver<Screen>
    interface Mutable: Update, Observe {
        fun clear()
    }

    class Base: UiObservable.Base<Screen>(Screen.Empty), Mutable
}