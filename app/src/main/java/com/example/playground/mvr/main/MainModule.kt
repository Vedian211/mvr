package com.example.playground.mvr.main

import com.example.playground.mvr.core.Core
import com.example.playground.mvr.core.Module

class MainModule(private val core: Core): Module<MainRepresentative> {
    override fun representative(): MainRepresentative {
        return MainRepresentative.Base(core.navigation())
    }

}