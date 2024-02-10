package com.kolee.composepedometer2.di

import android.app.NotificationManager
import android.content.Context
import android.hardware.SensorManager
import com.kolee.composepedometer2.room_db.PedometerDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePedometerDB(@ApplicationContext context: Context): PedometerDB {
        return PedometerDB.getInstance(context)
    }

    @Provides
    @Singleton
    fun provideNotificationManager(@ApplicationContext context: Context): NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @Singleton
    fun provideSensorManager(@ApplicationContext context: Context): SensorManager =
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

}