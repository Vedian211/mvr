package com.example.playground.mvr.playing.fakes

import com.example.playground.mvr.core.ClearRepresentative
import com.example.playground.mvr.core.HandleDeath
import com.example.playground.mvr.core.Representative
import com.example.playground.mvr.core.UiObserver
import com.example.playground.mvr.main.Navigation
import com.example.playground.mvr.main.Screen
import com.example.playground.mvr.subscription.SaveAndRestoreSubscriptionUiState
import com.example.playground.mvr.subscription.domain.SubscriptionInteractor
import com.example.playground.mvr.subscription.presentation.SubscriptionObservable
import com.example.playground.mvr.subscription.presentation.SubscriptionObserver
import com.example.playground.mvr.subscription.presentation.SubscriptionUiState
import org.junit.Assert

interface FakeSaveAndRestore: SaveAndRestoreSubscriptionUiState.Mutable {

    class Base: SaveAndRestoreSubscriptionUiState.Mutable {

        private var state: SubscriptionUiState? = null

        override fun save(data: SubscriptionUiState) {
            state = data
        }

        override fun restore(): SubscriptionUiState {
            return state!!
        }

        override fun isEmpty(): Boolean {
            return state == null
        }
    }
}

interface FakeNavigation: Navigation.Update {

    fun checkUpdated(screen: Screen)

    class Base: FakeNavigation {

        private var updateCalledWithScreen: Screen = Screen.Empty
        override fun checkUpdated(screen: Screen) {
            Assert.assertEquals(screen, updateCalledWithScreen)
        }

        override fun update(data: Screen) {
            updateCalledWithScreen = data
        }
    }
}

interface FakeInteractor: SubscriptionInteractor {

    fun pingCallback()
    fun checkSubscribeCalledTimes(times: Int)

    class Base: FakeInteractor {

        private var cachedCallback: () -> Unit = {}
        private var subscribeCountTime: Int = 0

        override fun checkSubscribeCalledTimes(times: Int) {
            Assert.assertEquals(times, subscribeCountTime)
        }

        override fun pingCallback() {
            cachedCallback.invoke()
        }

        override fun subscribe(callback: () -> Unit) {
            cachedCallback = callback
            subscribeCountTime ++
        }
    }
}


interface FakeClear: ClearRepresentative {

    fun checkClearCalledWith(clazz: Class<out Representative<*>>)

    class Base: FakeClear {

        private var clearCalledClazz: Class<out Representative<*>>? = null

        override fun checkClearCalledWith(clazz: Class<out Representative<*>>) {
            Assert.assertEquals(clazz, clearCalledClazz)
        }

        override fun clear(clazz: Class<out Representative<*>>) {
            clearCalledClazz = clazz
        }
    }
}

interface FakeObservable: SubscriptionObservable {

    fun checkClearCold()
    fun checkUpdateCalledCount(times: Int)
    fun checkUiState(expected: SubscriptionUiState)
    fun checkUpdateObserverCalled(observer: SubscriptionObserver)

    class Base: FakeObservable {

        private var clearCalled = false
        private var updateCalledCount = 0
        private var cache: SubscriptionUiState = SubscriptionUiState.Empty
        private var observerCached: UiObserver<SubscriptionUiState> = object :
            SubscriptionObserver {
            override fun update(data: SubscriptionUiState) = Unit
        }

        override fun checkUpdateCalledCount(times: Int) {
            Assert.assertEquals(times, updateCalledCount)
        }

        override fun checkClearCold() {
            Assert.assertEquals(true, clearCalled)
            clearCalled = false
        }

        override fun clear() {
            clearCalled = true
            cache = SubscriptionUiState.Empty
        }

        override fun update(data: SubscriptionUiState) {
            cache = data
            updateCalledCount ++
        }

        override fun checkUiState(expected: SubscriptionUiState) {
            Assert.assertEquals(expected, cache)
        }

        override fun checkUpdateObserverCalled(observer: SubscriptionObserver) {
            Assert.assertEquals(observer, observerCached)
        }

        override fun updateObserver(uiObserver: UiObserver<SubscriptionUiState>) {
            observerCached = uiObserver
        }

        override fun saveState(saveState: SaveAndRestoreSubscriptionUiState.Save) {
            saveState.save(cache)
        }

    }
}

interface FakeHandelDeath: HandleDeath {

    fun checkFirstOpeningCalled(times: Int)

    class Base: FakeHandelDeath {

        private var counter: Int = 0
        private var deathHappened = true

        override fun checkFirstOpeningCalled(times: Int) {
            Assert.assertEquals(times, counter)
        }

        override fun firstOpening() {
            deathHappened = false
            counter ++
        }

        override fun didDeathHappen(): Boolean {
            return deathHappened
        }

        override fun deathHandled() {
            deathHappened = false
        }
    }
}