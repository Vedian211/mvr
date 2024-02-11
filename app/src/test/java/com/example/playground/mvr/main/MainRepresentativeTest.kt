package com.example.playground.mvr.main

import com.example.playground.mvr.core.UiObserver
import org.junit.Assert.*
import org.junit.Test

class MainRepresentativeTest {

    class TestObjects {
        val navigation by lazy { FakeNavigation.Base() }
        val callback by lazy {
            object : ActivityCallback {
                override fun update(data: Screen) = Unit
            }
        }

        val representative by lazy { MainRepresentative.Base(navigation = navigation) }
    }

    @Test
    fun `Given first open when showDashboard then navigate to Dashboard`() = with(TestObjects()) {
        // When
        representative.startGettingUpdates(callback)
        representative.showDashboard(true)

        // Then
        navigation.checkUpdated(Screen.Dashboard)
        navigation.checkUpdateCalledCount(1)
        navigation.checkUpdatedObserverCalled(callback)
    }

    @Test
    fun `Given not first open when showDashboard then navigation update not invoked`() = with(TestObjects()) {
        // When
        representative.startGettingUpdates(callback)
        representative.showDashboard(false)

        // Then
        navigation.checkUpdateCalledCount(0)
        navigation.checkUpdatedObserverCalled(callback)
    }

    @Test
    fun `When stopGettingUpdates then verify EmptyMainObserver set`() = with(TestObjects()) {
        // When
        representative.stopGettingUpdates()

        // Then
        navigation.checkUpdatedObserverCalled(EmptyMainObserver)
    }
}

interface FakeNavigation: Navigation.Mutable {

    fun checkUpdated(screen: Screen)
    fun checkUpdateCalledCount(times: Int)
    fun checkUpdatedObserverCalled(observer: UiObserver<Screen>)

    class Base: FakeNavigation {

        private var clearCalled: Boolean = false
        private var cache: Screen = Screen.Empty
        private var updateCalledCounter = 0
        private var cachedObserver: UiObserver<Screen> = object : UiObserver<Screen> {
            override fun update(data: Screen) = Unit
        }

        override fun checkUpdateCalledCount(times: Int) {
            assertEquals(times, updateCalledCounter)
        }

        override fun checkUpdatedObserverCalled(observer: UiObserver<Screen>) {
            assertEquals(observer, cachedObserver)
        }

        override fun checkUpdated(screen: Screen) {
            assertEquals(screen, cache)
        }

        override fun clear() {
            cache = Screen.Empty
            clearCalled = true
        }

        override fun update(data: Screen) {
            updateCalledCounter ++
            cache = data
        }

        override fun updateObserver(uiObserver: UiObserver<Screen>) {
            cachedObserver = uiObserver
        }
    }
}