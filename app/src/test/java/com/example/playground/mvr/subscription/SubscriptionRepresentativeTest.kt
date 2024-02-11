package com.example.playground.mvr.subscription

import com.example.playground.mvr.core.ClearRepresentative
import com.example.playground.mvr.core.HandleDeath
import com.example.playground.mvr.core.Representative
import com.example.playground.mvr.core.UiObserver
import com.example.playground.mvr.main.Navigation
import com.example.playground.mvr.main.Screen
import com.example.playground.mvr.subscription.domain.SubscriptionInteractor
import com.example.playground.mvr.subscription.presentation.EmptySubscriptionObserver
import com.example.playground.mvr.subscription.presentation.SubscriptionObservable
import com.example.playground.mvr.subscription.presentation.SubscriptionObserver
import com.example.playground.mvr.subscription.presentation.SubscriptionRepresentative
import com.example.playground.mvr.subscription.presentation.SubscriptionUiState
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SubscriptionRepresentativeTest {

    private lateinit var representative: SubscriptionRepresentative
    private lateinit var fakeHandelDeath: FakeHandelDeath
    private lateinit var observable: FakeObservable
    private lateinit var interactor: FakeInteractor
    private lateinit var navigation: FakeNavigation
    private lateinit var clear: FakeClear

    @Before
    fun setup() {
        fakeHandelDeath = FakeHandelDeath.Base()
        observable = FakeObservable.Base()
        interactor = FakeInteractor.Base()
        navigation = FakeNavigation.Base()
        clear = FakeClear.Base()

        representative = SubscriptionRepresentative.Base(
            handleDeath = fakeHandelDeath,
            observable = observable,
            interactor = interactor,
            navigation = navigation,
            clear = clear
        )
    }

    @Test
    fun main_scenario() {
        val saveAndRestore = FakeSaveAndRestore.Base()
        representative.init(saveAndRestore)

        // asserts
        fakeHandelDeath.checkFirstOpeningCalled(1)
        observable.checkUiState(SubscriptionUiState.Initial)
        observable.checkUpdateCalledCount(1)

        val callback = object : SubscriptionObserver {
            override fun update(data: SubscriptionUiState) = Unit
        }
        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)

        representative.subscribe()
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)
        interactor.pingCallback()
        observable.checkUiState(SubscriptionUiState.Success)

        representative.observed()
        observable.checkClearCold()

        representative.finish()
        clear.clear(SubscriptionRepresentative::class.java)
        navigation.checkUpdated(Screen.Dashboard)

        representative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
    }

    @Test
    fun test_save_and_restore() {
        val saveAndRestore = FakeSaveAndRestore.Base()
        representative.init(saveAndRestore)
        fakeHandelDeath.checkFirstOpeningCalled(1)
        observable.checkUiState(SubscriptionUiState.Initial)
        observable.checkUpdateCalledCount(1)

        val callback = object : SubscriptionObserver {
            override fun update(data: SubscriptionUiState) = Unit
        }
        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)

        representative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
        representative.saveState(saveAndRestore)

        representative.init(saveAndRestore)
        fakeHandelDeath.checkFirstOpeningCalled(1)
        observable.checkUpdateCalledCount(1)
    }

    @Test
    fun test_death() {
        val saveAndRestore = FakeSaveAndRestore.Base()
        representative.init(saveAndRestore)

        // asserts
        fakeHandelDeath.checkFirstOpeningCalled(1)
        observable.checkUiState(SubscriptionUiState.Initial)

        val callback = object : SubscriptionObserver {
            override fun update(data: SubscriptionUiState) = Unit
        }
        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)

        representative.subscribe()
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)
        representative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
        representative.saveState(saveAndRestore)

        // death
        setup()

        representative.init(saveAndRestore)
        fakeHandelDeath.checkFirstOpeningCalled(0)
        observable.checkUiState(SubscriptionUiState.Empty)
        observable.checkUpdateCalledCount(0)
        interactor.checkSubscribeCalledTimes(1)
    }

    @Test
    fun test_death_after_success() {
        val saveAndRestore = FakeSaveAndRestore.Base()
        representative.init(saveAndRestore)

        // asserts
        fakeHandelDeath.checkFirstOpeningCalled(1)
        observable.checkUiState(SubscriptionUiState.Initial)

        val callback = object : SubscriptionObserver {
            override fun update(data: SubscriptionUiState) = Unit
        }
        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)

        representative.subscribe()
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)
        interactor.pingCallback()
        observable.checkUiState(SubscriptionUiState.Success)
        representative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
        representative.saveState(saveAndRestore)

        // death
        setup()

        representative.init(saveAndRestore)
        fakeHandelDeath.checkFirstOpeningCalled(0)
        observable.checkUiState(SubscriptionUiState.Success)
        observable.checkUpdateCalledCount(1)
        interactor.checkSubscribeCalledTimes(0)
    }

    @Test
    fun test_death_after_success_observed() {
        val saveAndRestore = FakeSaveAndRestore.Base()
        representative.init(saveAndRestore)

        // asserts
        fakeHandelDeath.checkFirstOpeningCalled(1)
        observable.checkUiState(SubscriptionUiState.Initial)

        val callback = object : SubscriptionObserver {
            override fun update(data: SubscriptionUiState) = Unit
        }
        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)

        representative.subscribe()
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)
        interactor.pingCallback()
        observable.checkUiState(SubscriptionUiState.Success)
        representative.observed()
        observable.checkClearCold()
        representative.stopGettingUpdates()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
        representative.saveState(saveAndRestore)

        // death
        setup()

        representative.init(saveAndRestore)
        fakeHandelDeath.checkFirstOpeningCalled(0)
        observable.checkUiState(SubscriptionUiState.Empty)
        observable.checkUpdateCalledCount(0)
        interactor.checkSubscribeCalledTimes(0)
    }

}

private interface FakeSaveAndRestore: SaveAndRestoreSubscriptionUiState.Mutable {

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

private interface FakeNavigation: Navigation.Update {

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

private interface FakeInteractor: SubscriptionInteractor {

    fun pingCallback()
    fun checkSubscribeCalledTimes(times: Int)

    class Base: FakeInteractor {

        private var cachedCallback: () -> Unit = {}
        private var subscribeCountTime: Int = 0

        override fun checkSubscribeCalledTimes(times: Int) {
            assertEquals(times, subscribeCountTime)
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


private interface FakeClear: ClearRepresentative {

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

private interface FakeObservable: SubscriptionObservable {

    fun checkClearCold()
    fun checkUpdateCalledCount(times: Int)
    fun checkUiState(expected: SubscriptionUiState)
    fun checkUpdateObserverCalled(observer: SubscriptionObserver)

    class Base: FakeObservable {

        private var clearCalled = false
        private var updateCalledCount = 0
        private var cache: SubscriptionUiState = SubscriptionUiState.Empty
        private var observerCached: UiObserver<SubscriptionUiState> = object : SubscriptionObserver {
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

private interface FakeHandelDeath: HandleDeath {

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