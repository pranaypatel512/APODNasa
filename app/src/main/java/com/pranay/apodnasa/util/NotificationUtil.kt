package com.pranay.apodnasa.util

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.pranay.apodnasa.R

/**
 * Create the notification and required channel (O+) for running work in a foreground service.
 */
fun createNotification(context: Context, notificationTitle: String):
        Notification {
    val channelId = context.getString(R.string.app_name)
    val name = context.getString(R.string.notification_title_getting_images)

    val builder = NotificationCompat.Builder(context, channelId)
        .setContentTitle(notificationTitle)
        .setTicker(notificationTitle)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setOngoing(true)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        createNotificationChannel(context, channelId, name).also {
            builder.setChannelId(it.id)
        }
    }
    return builder.build()
}

/**
 * Create the required notification channel for O+ devices.
 */
@TargetApi(Build.VERSION_CODES.O)
fun createNotificationChannel(
    context: Context,
    channelId: String,
    name: String,
    notificationImportance: Int = NotificationManager.IMPORTANCE_HIGH
): NotificationChannel {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return NotificationChannel(
        channelId, name, notificationImportance
    ).also { channel ->
        notificationManager.createNotificationChannel(channel)
    }
}