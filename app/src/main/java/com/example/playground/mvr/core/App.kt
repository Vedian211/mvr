package com.example.playground.mvr.core

import android.app.Application
import com.example.playground.mvr.core.ProvideRepresentative.MakeDependency

class App: Application(), ProvideRepresentative, ClearRepresentative {

    private lateinit var factory: ProvideRepresentative.Factory

    override fun onCreate() {
        super.onCreate()
        factory = ProvideRepresentative.Factory(
            makeDependency = MakeDependency(core = Core.Base(context = this), clear = this)
        )
    }

    override fun <T : Representative<*>> provideRepresentative(clazz: Class<T>): T = factory.provideRepresentative(clazz)

    override fun clear(clazz: Class<out Representative<*>>) {
        factory.clear(clazz)
    }
}