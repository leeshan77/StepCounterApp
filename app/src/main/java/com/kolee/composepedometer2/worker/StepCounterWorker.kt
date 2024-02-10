package com.kolee.composepedometer2.worker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.kolee.composepedometer2.common.Constants.NOTIFICATION_CHANNEL
import com.kolee.composepedometer2.room_db.StepsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import com.kolee.composepedometer2.R.string as AppString
import com.kolee.composepedometer2.R.drawable as AppDrawable

@HiltWorker
class StepCounterWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    @Inject
    lateinit var stepsRepository: StepsRepository
    @Inject
    lateinit var sensorManager: SensorManager

    override suspend fun doWork(): Result {

        setForeground(getForegroundInfo())

        val stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        return if (stepCounterSensor != null) {
            val success = getStepsCount(sensorManager, stepCounterSensor)
            if (success) Result.success() else Result.retry()
        } else {
            Result.failure()
        }
    }

    private suspend fun getStepsCount(
        sensorManager: SensorManager,
        stepCounterSensor: Sensor
    ) = suspendCoroutine<Boolean> { continuation ->
        val listener = object : SensorEventListener {

            override fun onSensorChanged(event: SensorEvent?) {
                // Make sure to remove listener to avoid wasting resources
                sensorManager.unregisterListener(this)

                event?.values?.firstOrNull()?.let { steps ->
                    val stepsToday = stepsRepository.updateStepsSinceBoot(steps.toLong())
                    Log.d(TAG, "Step count: $steps, steps today: $stepsToday")
                    continuation.resume(true)
                    return
                }
                continuation.resume(false)
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                Log.d(TAG, sensor?.name + " accuracy changed: " + accuracy)
            }
        }

        sensorManager.registerListener(
            listener,
            stepCounterSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        val title = applicationContext.getString(AppString.notification_title)
        val content = applicationContext.getString(AppString.worker_notification_content)

        val notification = NotificationCompat.Builder(applicationContext, NOTIFICATION_CHANNEL)
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(AppDrawable.baseline_notifications_24)
            .setOngoing(true)
            .build()

        return ForegroundInfo(WORKER_NOTIFY_ID, notification)
    }

    companion object {
        private const val TAG = "StepsCounterWorker"
        private const val WORKER_NOTIFY_ID = 567348

        fun periodicWork(context: Context) {
            val workRequest = PeriodicWorkRequestBuilder<StepCounterWorker>(
                15, TimeUnit.MINUTES
            )
                .addTag("step_counter_Worker")
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "periodic_pedometer_Worker",
                ExistingPeriodicWorkPolicy.REPLACE,
                workRequest
            )
        }
    }
}