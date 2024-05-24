package com.example.food_app.data.Notification

import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import com.example.food_app.R
import kotlin.random.Random

class FoodNotificationService(
    private  val context :Context
) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    fun showBasicNotification(){
        val notification = NotificationCompat.Builder(context,"food_orders")
            .setContentTitle("Food Delivery")
            .setContentText("Thanks for your order, please wait for a call from the shipper")
            .setSmallIcon(R.drawable.happy)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)
            .build()
        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }
    fun showExpandableNotification(
        icon : Int,
        title : String,
        content : String
    ){
        val image = when ((0..3).random()) {
            0 -> context.bitmapFromResource(R.drawable.delivery)
            1 -> context.bitmapFromResource(R.drawable.delivery2)
            2 -> context.bitmapFromResource(R.drawable.delivery4)
            else -> context.bitmapFromResource(R.drawable.delivery3)
        }

        val notification = NotificationCompat.Builder(context,"food_orders")
            .setContentTitle(title)
            .setContentText(content)
            .setSmallIcon(icon)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setLargeIcon(image)
            .setStyle(
                NotificationCompat
                    .BigPictureStyle()
                    .bigPicture(image)
                    .bigLargeIcon(null as Bitmap?)
            )
            .setAutoCancel(true)
            .build()
        notificationManager.notify(
            Random.nextInt(),
            notification
        )
    }
    private  fun Context.bitmapFromResource(
        @DrawableRes resId :Int
    ) = BitmapFactory.decodeResource(
        resources,
        resId
    )
}