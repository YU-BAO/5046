package com.example.a5046a3.worker

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.a5046a3.R
import com.example.a5046a3.api.WeatherApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DailyReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val response = WeatherApi.retrofitService.getCurrentWeather(
                lat = -37.81,
                lon = 144.96,
                appid = com.example.a5046a3.BuildConfig.OPENWEATHER_API_KEY
            )

            val temperature = response.body()?.main?.temp
            Log.d("WeatherDebug", "Fetched in Worker: $temperature°C")

            if (temperature != null) {
                showNotification(
                    title = "Weather Reminder",
                    message = "Today's temperature: $temperature°C"
                )
            } else {
                showNotification(
                    title = "Weather Reminder",
                    message = "Unable to fetch temperature"
                )
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("WeatherDebug", "Worker failed:", e)
            Result.failure()
        }
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "weather_channel"

        // Android 8.0+ requires a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Weather Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification) // Ensure that this icon exists
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(1001, builder.build())
            Log.d("WeatherDebug", "Notification sent")
        } else {
            Log.w("WeatherDebug", "No notification permission")
        }
    }
}
