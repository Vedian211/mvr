package com.example.playground.mvr.core

interface ClearRepresentative {

    fun clear(clazz: Class<out Representative<*>>)
}