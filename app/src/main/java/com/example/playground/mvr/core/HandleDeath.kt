package com.example.playground.mvr.core

interface HandleDeath {

    fun firstOpening()
    fun didDeathHappen(): Boolean
    fun deathHandled()
    class Base: HandleDeath {

        private var deathHappened = true
        override fun firstOpening() {
            deathHappened = false
        }

        override fun didDeathHappen(): Boolean {
            return deathHappened
        }

        override fun deathHandled() {
            deathHappened = false
        }
    }
}