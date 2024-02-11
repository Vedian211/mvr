package com.example.playground.mvr.subscription

import com.example.playground.mvr.main.Screen
import com.example.playground.mvr.playing.fakes.FakeClear
import com.example.playground.mvr.playing.fakes.FakeHandelDeath
import com.example.playground.mvr.playing.fakes.FakeInteractor
import com.example.playground.mvr.playing.fakes.FakeNavigation
import com.example.playground.mvr.playing.fakes.FakeObservable
import com.example.playground.mvr.playing.fakes.FakeRunAsync
import com.example.playground.mvr.playing.fakes.FakeSaveAndRestore
import com.example.playground.mvr.subscription.presentation.EmptySubscriptionObserver
import com.example.playground.mvr.subscription.presentation.SubscriptionObserver
import com.example.playground.mvr.subscription.presentation.SubscriptionRepresentative
import com.example.playground.mvr.subscription.presentation.SubscriptionUiState
import org.junit.Before
import org.junit.Test

class SubscriptionRepresentativeTest {

    private lateinit var representative: SubscriptionRepresentative
    private lateinit var fakeHandelDeath: FakeHandelDeath
    private lateinit var observable: FakeObservable
    private lateinit var interactor: FakeInteractor
    private lateinit var navigation: FakeNavigation
    private lateinit var runAsync: FakeRunAsync
    private lateinit var clear: FakeClear

    @Before
    fun setup() {
        fakeHandelDeath = FakeHandelDeath.Base()
        observable = FakeObservable.Base()
        interactor = FakeInteractor.Base()
        navigation = FakeNavigation.Base()
        runAsync = FakeRunAsync.Base()
        clear = FakeClear.Base()

        representative = SubscriptionRepresentative.Base(
            handleDeath = fakeHandelDeath,
            observable = observable,
            interactor = interactor,
            navigation = navigation,
            runAsync = runAsync,
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
        runAsync.pingResult()
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
        runAsync.pingResult()
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
        runAsync.pingResult()
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

    @Test
    fun test_cannot_go_back() {
        val saveAndRestore = FakeSaveAndRestore.Base()
        representative.init(saveAndRestore)
        val callback = object : SubscriptionObserver {
            override fun update(data: SubscriptionUiState) = Unit
        }

        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)

        representative.subscribe()
        representative.comeback()

        runAsync.checkClearCalledTimes(0)
    }

    @Test
    fun test_can_go_back() {
        val saveAndRestore = FakeSaveAndRestore.Base()
        representative.init(saveAndRestore)
        val callback = object : SubscriptionObserver {
            override fun update(data: SubscriptionUiState) = Unit
        }

        representative.startGettingUpdates(callback)
        observable.checkUpdateObserverCalled(callback)

        representative.subscribe()
        runAsync.pingResult()
        representative.comeback()

        runAsync.checkClearCalledTimes(1)
    }
}