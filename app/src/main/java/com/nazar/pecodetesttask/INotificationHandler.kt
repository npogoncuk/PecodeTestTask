package com.nazar.pecodetesttask

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.app.NotificationCompat

interface INotificationHandler {

    companion object {
        const val pendingIntentExtrasPageKey = "page_number"
    }

    fun sendNotification(channelId: Int)

    fun cancelAllNotifications(channelId: Int)

    class Default(private val context: Context) : INotificationHandler {

        private val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        override fun sendNotification(channelId: Int) {
            createNotificationChanelInNeeded(channelId)

            val notificationIntent = Intent(context, MainActivity::class.java).apply {
                setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                putExtras(Bundle().apply {
                    putInt(pendingIntentExtrasPageKey, channelId)
                })
            }

            val pendingIntent = PendingIntent.getActivity(context, channelId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

            val notification = NotificationCompat.Builder(context, channelId.toString())
                .setSmallIcon(R.drawable.default_notification_icon)
                .setContentTitle(context.getString(R.string.default_notification_title))
                .setContentText(context.getString(R.string.default_notification_text, channelId + 1))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

            notificationManager.notify(channelId, notification)
        }

        private fun createNotificationChanelInNeeded(channelId: Int) {
            fun createNotificationChannel(): NotificationChannel {
                val name = "chanel_name"//getString(R.string.channel_name)
                val descriptionText = "description"//getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_HIGH
                val channel = NotificationChannel(channelId.toString(), name, importance)
                return channel
            }

            notificationManager.getNotificationChannel(channelId.toString()) ?: run {
                val newChanel = createNotificationChannel()
                notificationManager.createNotificationChannel(newChanel)
            }
        }

        override fun cancelAllNotifications(channelId: Int) {
            notificationManager.deleteNotificationChannel(channelId.toString())
        }

    }
}