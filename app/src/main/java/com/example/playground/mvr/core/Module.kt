package com.example.playground.mvr.core

interface Module<T: Representative<*>> {

    fun representative(): T
}