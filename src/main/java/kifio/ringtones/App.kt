package kifio.ringtones

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.annotation.TargetApi

class App : Application() {

    companion object {
        val CHANNEL_ID = "rrc"
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            buildOreoNotificationChannel()
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun buildOreoNotificationChannel() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val name = "ringtones_randomizer_channel"
        val description = "Ringtones randomizer notifications channel."
        val priority = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(CHANNEL_ID, name, priority)
        channel.enableLights(true)
        channel.description = description
        channel.setShowBadge(true)
        manager.createNotificationChannel(channel)
    }
}