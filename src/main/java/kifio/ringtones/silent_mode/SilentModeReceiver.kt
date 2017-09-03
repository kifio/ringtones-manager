package kifio.ringtones.silent_mode

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.support.v7.app.NotificationCompat
import kifio.ringtones.App
import kifio.ringtones.MainActivity
import kifio.ringtones.R
import java.util.*

class SilentModeReceiver : BroadcastReceiver() {

    override fun onReceive(ctx: Context, intent: Intent) {

        val manager = ctx.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val enable = intent.getBooleanExtra("enable", false)

        manager.ringerMode = if (enable) {
            AudioManager.RINGER_MODE_VIBRATE
        } else {
            AudioManager.RINGER_MODE_NORMAL
        }

        showNotification(ctx, enable)
    }

    private fun showNotification(ctx: Context, enable: Boolean) {

        val manager = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val requestCode = 0
        val intent = PendingIntent.getActivity(ctx, requestCode,
                Intent(ctx, MainActivity::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(ctx)
                .setSmallIcon(R.drawable.ic_vibration)
                .setContentTitle("Ringtone for today")
                .setContentText(if (enable) "Vibration on" else "Vibration off")
                .setChannelId(App.CHANNEL_ID)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setBadgeIconType(Notification.BADGE_ICON_SMALL)
        }

        builder.setContentIntent(intent)
        manager.notify(Random(System.currentTimeMillis()).nextInt(), builder.build())
    }

}