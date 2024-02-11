package com.example.playground.mvr.core

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.playground.mvr.main.Navigation

interface ProvideSharedPreferences {
    fun sharedPreferences(): SharedPreferences
}

interface ProvideNavigation {
    fun navigation(): Navigation.Mutable
}

interface ProvideRunAsync {
    fun runAsync(): RunAsync
}

interface Core: ProvideNavigation, ProvideSharedPreferences, ProvideRunAsync {

    class Base(private val context: Context): Core {

        private val navigation = Navigation.Base()
        private val runAsync = RunAsync.Base(dispatchersList = DispatchersList.Base())

        override fun navigation(): Navigation.Mutable = navigation
        override fun sharedPreferences(): SharedPreferences = context.getSharedPreferences("project999", MODE_PRIVATE)
        override fun runAsync(): RunAsync = runAsync
    }
}