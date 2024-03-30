package com.example.playground.mvr.subscription.fakes

import com.example.playground.mvr.core.ClearRepresentative
import com.example.playground.mvr.core.HandleDeath
import com.example.playground.mvr.core.Representative
import com.example.playground.mvr.core.RunAsync
import com.example.playground.mvr.core.UiObserver
import com.example.playground.mvr.main.Navigation
import com.example.playground.mvr.main.Screen
import com.example.playground.mvr.subscription.screen.presentation.SaveAndRestoreSubscriptionUiState
import com.example.playground.mvr.subscription.progress.domain.SubscriptionInteractor
import com.example.playground.mvr.subscription.progress.domain.SubscriptionResult
import com.example.playground.mvr.subscription.screen.presentation.SubscriptionObservable
import com.example.playground.mvr.subscription.screen.presentation.SubscriptionObserver
import com.example.playground.mvr.subscription.screen.presentation.SubscriptionUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

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
            assertEquals(screen, updateCalledWithScreen)
        }

        override fun update(data: Screen) {
            updateCalledWithScreen = data
        }
    }
}

interface FakeInteractor: SubscriptionInteractor {

    fun checkSubscribeCalledTimes(times: Int)

    class Base: FakeInteractor {

        private var subscribeCountTime: Int = 0

        override fun checkSubscribeCalledTimes(times: Int) {
            assertEquals(times, subscribeCountTime)
        }

        override suspend fun subscribe(): SubscriptionResult {
            subscribeCountTime ++
            return SubscriptionResult.NoDataYet
        }

        override suspend fun subscribeInternal(): SubscriptionResult {
            return SubscriptionResult.Success
        }
    }
}

interface FakeRunAsync: RunAsync {

    fun checkClearCalledTimes(times: Int)
    fun pingResult()

    @Suppress("UNCHECKED_CAST")
    class Base: FakeRunAsync {

        private var cachedBlock: (Any) -> Unit = { }
        private var cached: Any = Unit
        private var clearCounter: Int = 0

        override fun checkClearCalledTimes(times: Int) {
            assertEquals(times, clearCounter)
        }

        override fun pingResult() {
            cachedBlock.invoke(cached)
        }

        override fun <T : Any> runAsync(
            scope: CoroutineScope,
            backgroundBlock: suspend () -> T,
            uiBlock: (T) -> Unit
        ) = runBlocking {
            cached = backgroundBlock.invoke()
            cachedBlock = uiBlock as ((Any) -> Unit)
        }

        override suspend fun <T : Any> runAsync(
            backgroundBlock: suspend () -> T,
            uiBlock: (T) -> Unit
        ) {
            uiBlock.invoke(backgroundBlock.invoke())
        }

        override fun clear() {
            clearCounter ++
        }

    }
}

interface FakeClear: ClearRepresentative {

    fun checkClearCalledWith(clazz: Class<out Representative<*>>)

    class Base: FakeClear {

        private var clearCalledClazz: Class<out Representative<*>>? = null

        override fun checkClearCalledWith(clazz: Class<out Representative<*>>) {
            assertEquals(clazz, clearCalledClazz)
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
            assertEquals(times, updateCalledCount)
        }

        override fun checkClearCold() {
            assertEquals(true, clearCalled)
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
            assertEquals(expected, cache)
        }

        override fun checkUpdateObserverCalled(observer: SubscriptionObserver) {
            assertEquals(observer, observerCached)
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
            assertEquals(times, counter)
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

interface FakeMapper: SubscriptionResult.Mapper {

    class Base(private val observable: FakeObservable): FakeMapper {
        override fun mapSuccess(canGoBackCallback: (Boolean) -> Unit) {
            observable.update(SubscriptionUiState.Success)
            canGoBackCallback.invoke(true)
        }
    }
}