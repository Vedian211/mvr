package com.example.playground.mvr.dashboard

import com.example.playground.mvr.core.UiObserver
import com.example.playground.mvr.dashboard.DashboardRepresentative.*
import com.example.playground.mvr.main.Screen
import com.example.playground.mvr.subscription.fakes.FakeClear
import com.example.playground.mvr.subscription.fakes.FakeNavigation
import org.junit.Assert.*
import org.junit.Test

class DashboardRepresentationTest {

    class TestObjects {
        val navigation by lazy { FakeNavigation.Base() }
        val observable by lazy { FakeObservable.Base() }
        val clear by lazy { FakeClear.Base() }
        val callback by lazy {
            object : DashboardObserver {
                override fun update(data: PremiumDashboardUiState) = Unit
            }
        }

        val baseRepresentative by lazy { Base(navigation = navigation) }
        val premiumRepresentative by lazy {
            Premium(
                observable = observable,
                clear = clear
            )
        }
    }

    @Test
    fun `Given Premium user when startGettingUpdate then verify callback invoked`() = with(TestObjects()) {
        // When
        premiumRepresentative.startGettingUpdates(callback)

        // Then
        observable.checkUpdateObserverCalled(callback)
    }

    @Test
    fun `Given Premium user when stopGettingUpdate then verify EmptyDashboardObserver set`() = with(TestObjects()) {
        // When
        premiumRepresentative.stopGettingUpdates()

        // Then
        observable.checkUpdateObserverCalled(EmptyDashboardObserver)
    }

    @Test
    fun `Given Base user when play then navigate to subscription`() = with(TestObjects()) {
        // When
        baseRepresentative.play()

        // Then
        navigation.checkUpdated(Screen.Subscription)
    }

    @Test
    fun `Given Premium user when play then Playing state set and clear invoked`() = with(TestObjects()) {
        // When
        premiumRepresentative.play()

        // Then
        clear.checkClearCalledWith(DashboardRepresentative::class.java)
        observable.checkUiState(PremiumDashboardUiState.Playing)
        observable.checkUpdateCalledCount(1)
    }

    interface FakeObservable: PremiumDashboardObservable {

        fun checkClearCold()
        fun checkUpdateCalledCount(times: Int)
        fun checkUiState(expected: PremiumDashboardUiState)
        fun checkUpdateObserverCalled(observer: UiObserver<PremiumDashboardUiState>)

        class Base: FakeObservable {

            private var clearCalled = false
            private var updateCalledCount = 0
            private var cache: PremiumDashboardUiState = PremiumDashboardUiState.Empty
            private var cachedObserver: UiObserver<PremiumDashboardUiState> = UiObserver.Empty()

            override fun clear() {
                clearCalled = true
                cache = PremiumDashboardUiState.Empty
            }

            override fun update(data: PremiumDashboardUiState) {
                cache = data
                updateCalledCount ++
            }

            override fun updateObserver(uiObserver: UiObserver<PremiumDashboardUiState>) {
                cachedObserver = uiObserver
            }

            override fun checkClearCold() {
                assertEquals(true, clearCalled)
            }

            override fun checkUpdateCalledCount(times: Int) {
                assertEquals(times, updateCalledCount)
            }

            override fun checkUiState(expected: PremiumDashboardUiState) {
                assertEquals(expected, cache)
            }

            override fun checkUpdateObserverCalled(observer: UiObserver<PremiumDashboardUiState>) {
                assertEquals(observer, cachedObserver)
            }
        }
    }
}