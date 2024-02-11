package com.example.playground.mvr.subscription

import com.example.playground.mvr.main.Screen
import com.example.playground.mvr.subscription.presentation.EmptySubscriptionObserver
import com.example.playground.mvr.subscription.presentation.SubscriptionObserver
import com.example.playground.mvr.subscription.presentation.SubscriptionRepresentative
import com.example.playground.mvr.subscription.presentation.SubscriptionUiState
import com.example.playground.mvr.subscription.fakes.FakeClear
import com.example.playground.mvr.subscription.fakes.FakeHandelDeath
import com.example.playground.mvr.subscription.fakes.FakeInteractor
import com.example.playground.mvr.subscription.fakes.FakeNavigation
import com.example.playground.mvr.subscription.fakes.FakeObservable
import com.example.playground.mvr.subscription.fakes.FakeRunAsync
import com.example.playground.mvr.subscription.fakes.FakeSaveAndRestore
import org.junit.Test

class SubscriptionRepresentativeTest {

    class TestObjects {
        val saveAndRestore by lazy { FakeSaveAndRestore.Base() }
        val fakeHandelDeath by lazy { FakeHandelDeath.Base() }
        val observable by lazy { FakeObservable.Base() }
        val interactor by lazy { FakeInteractor.Base() }
        val navigation by lazy { FakeNavigation.Base() }
        val runAsync by lazy { FakeRunAsync.Base() }
        val clear by lazy { FakeClear.Base() }
        val callback by lazy {
            object : SubscriptionObserver {
                override fun update(data: SubscriptionUiState) = Unit
            }
        }
        val representative by lazy {
            SubscriptionRepresentative.Base(
                handleDeath = fakeHandelDeath,
                observable = observable,
                interactor = interactor,
                navigation = navigation,
                runAsync = runAsync,
                clear = clear
            )
        }
    }

    @Test
    fun `When init and start getting update then ui state is initial and callback invoked`() = with(
        TestObjects()
    ) {
        // When
        representative.init(saveAndRestore)
        representative.startGettingUpdates(callback)

        // Then
        fakeHandelDeath.checkFirstOpeningCalled(1)
        observable.checkUiState(SubscriptionUiState.Initial)
        observable.checkUpdateCalledCount(1)
        observable.checkUpdateObserverCalled(callback)
    }

    @Test
    fun `Given representative is initialized when subscribe then verify ui state`() = with(
        TestObjects()
    ) {
        // When
        representative.subscribe()

        // Then
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)
        runAsync.pingResult()
        observable.checkUiState(SubscriptionUiState.Success)
    }

    @Test
    fun `Given subscribed state when finish then navigate to Dashboard and clear resources`() = with(
        TestObjects()
    ) {
        // When
        representative.finish()
        representative.stopGettingUpdates()

        // Then
        clear.clear(SubscriptionRepresentative::class.java)
        navigation.checkUpdated(Screen.Dashboard)
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)
    }

    @Test
    fun `When re-init after restore then verify first open and subscription update happened once`() = with(
        TestObjects()
    ) {
        // When
        representative.init(saveAndRestore)
        representative.startGettingUpdates(callback)

        // Then
        fakeHandelDeath.checkFirstOpeningCalled(1)
        observable.checkUiState(SubscriptionUiState.Initial)
        observable.checkUpdateCalledCount(1)
        observable.checkUpdateObserverCalled(callback)

        // When
        representative.stopGettingUpdates()
        representative.saveState(saveAndRestore)

        // Then
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)

        // when
        representative.init(saveAndRestore)

        // Then
        fakeHandelDeath.checkFirstOpeningCalled(1)
        observable.checkUpdateCalledCount(1)
    }

    @Test
    fun `Given death happened on loading step when re-init then death handled and state Empty state restored`() = with(
        TestObjects()
    ) {
        // When
        representative.init(saveAndRestore)
        representative.startGettingUpdates(callback)

        // Then
        fakeHandelDeath.checkFirstOpeningCalled(1)
        observable.checkUiState(SubscriptionUiState.Initial)
        observable.checkUpdateObserverCalled(callback)

        // When
        representative.subscribe()

        // Then
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)

        // When
        representative.stopGettingUpdates()
        representative.saveState(saveAndRestore)

        // Then
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)

        // death
        // Given
        val testObject = TestObjects()

        // When
        testObject.representative.init(saveAndRestore)

        // Then
        testObject.fakeHandelDeath.checkFirstOpeningCalled(0)
        testObject.observable.checkUiState(SubscriptionUiState.Empty)
        testObject.observable.checkUpdateCalledCount(0)
        testObject.interactor.checkSubscribeCalledTimes(1)
    }

    @Test
    fun `Given death happened on Success step when re-init then death handled and Success state restored`() = with(
        TestObjects()
    ) {
        // When
        representative.init(saveAndRestore)
        representative.startGettingUpdates(callback)

        // Then
        fakeHandelDeath.checkFirstOpeningCalled(1)
        observable.checkUiState(SubscriptionUiState.Initial)
        observable.checkUpdateObserverCalled(callback)

        // When
        representative.subscribe()

        // Then
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)
        runAsync.pingResult()
        observable.checkUiState(SubscriptionUiState.Success)

        // When
        representative.stopGettingUpdates()
        representative.saveState(saveAndRestore)

        // Then
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)

        // death
        // Given
        val testObject = TestObjects()

        // When
        testObject.representative.init(saveAndRestore)

        // Then
        testObject.fakeHandelDeath.checkFirstOpeningCalled(0)
        testObject.observable.checkUiState(SubscriptionUiState.Success)
        testObject.observable.checkUpdateCalledCount(1)
        testObject.interactor.checkSubscribeCalledTimes(0)
    }

    @Test
    fun `Given death happened after success when re-init then death handled and any updates not called`() = with(
        TestObjects()
    ) {
        // When
        representative.init(saveAndRestore)
        representative.startGettingUpdates(callback)

        // Then
        fakeHandelDeath.checkFirstOpeningCalled(1)
        observable.checkUiState(SubscriptionUiState.Initial)
        observable.checkUpdateObserverCalled(callback)

        // When
        representative.subscribe()

        // Then
        observable.checkUiState(SubscriptionUiState.Loading)
        interactor.checkSubscribeCalledTimes(1)
        runAsync.pingResult()
        observable.checkUiState(SubscriptionUiState.Success)

        // When
        representative.observed()
        representative.stopGettingUpdates()
        representative.saveState(saveAndRestore)

        // Then
        observable.checkClearCold()
        observable.checkUpdateObserverCalled(EmptySubscriptionObserver)

        // death
        // Given
        val testObject = TestObjects()

        // When
        testObject.representative.init(saveAndRestore)

        // Then
        testObject.fakeHandelDeath.checkFirstOpeningCalled(0)
        testObject.observable.checkUiState(SubscriptionUiState.Empty)
        testObject.observable.checkUpdateCalledCount(0)
        testObject.interactor.checkSubscribeCalledTimes(0)
    }

    @Test
    fun `Given Loading state when comeback then clear not invoked`() = with(TestObjects()) {
        // When
        representative.init(saveAndRestore)
        representative.startGettingUpdates(callback)
        representative.subscribe()
        representative.comeback()

        // Then
        runAsync.checkClearCalledTimes(0)
    }

    @Test
    fun `Given Loading finished when comeback then clear invoked`() = with(TestObjects()) {
        // When
        representative.init(saveAndRestore)
        representative.startGettingUpdates(callback)
        representative.subscribe()
        runAsync.pingResult()
        representative.comeback()

        // Then
        runAsync.checkClearCalledTimes(1)
    }
}