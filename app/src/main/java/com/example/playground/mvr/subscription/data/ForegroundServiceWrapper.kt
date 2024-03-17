package com.example.playground.mvr.subscription.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.playground.mvr.core.ProvideRepresentative
import com.example.playground.mvr.subscription.presentation.SubscriptionRepresentative

interface ForegroundServiceWrapper {

    fun start()

    class Base(
        private val workManager: WorkManager
    ): ForegroundServiceWrapper {
        override fun start() {
            workManager.beginUniqueWork(
                WORK_NAME,
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequestBuilder<Worker>().build()
            ).enqueue()
        }
    }
}

private const val WORK_NAME = "subscription load async"
class Worker(
    appContext: Context,
    parameters: WorkerParameters
): CoroutineWorker(
    appContext = appContext,
    params = parameters
) {
    override suspend fun doWork(): Result {
        val representative = (applicationContext as ProvideRepresentative).provideRepresentative(SubscriptionRepresentative::class.java)
        representative.subscribeInternal()
        return Result.success()
    }
}