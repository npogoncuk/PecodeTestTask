package com.nazar.pecodetesttask

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

interface INotificationHandler {
    fun sendNotification(channelId: Int)

    fun cancelAllNotifications(channelId: Int)

    class Default(private val context: Context) : INotificationHandler {

        private val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        override fun sendNotification(channelId: Int) {
            createNotificationChanelInNeeded(channelId)

            val notification = NotificationCompat.Builder(context, channelId.toString())
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("textTitle")
                .setContentText("textContent")
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