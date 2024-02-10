package com.kolee.composepedometer2.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kolee.composepedometer2.room_db.StepsRepository
import com.kolee.composepedometer2.worker.StepCounterWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RebootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var stepsRepository: StepsRepository

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        // Make sure to reset the steps since boot in the db
        stepsRepository.updateStepsSinceBoot(0)

        StepCounterWorker.periodicWork(context)
    }
}