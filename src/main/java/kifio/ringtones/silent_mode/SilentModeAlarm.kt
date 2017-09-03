package kifio.ringtones.silent_mode

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*

object SilentModeAlarm {

    fun scheduleVibrationMode(ctx: Context, from: Int, to: Int) {
        enableAlarm(ctx, from, true, 0)
        enableAlarm(ctx, to, false, 1)
    }

    private fun enableAlarm(ctx: Context, minutes: Int, enable: Boolean, requestCode: Int) {
        disableAlarm(ctx, enable, requestCode)

        val manager: AlarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(ctx, SilentModeReceiver::class.java).putExtra("enable", enable)
        val alarmIntent = PendingIntent.getBroadcast(ctx, requestCode, intent, 0)
        val calendar = getCalendar(minutes)

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY, alarmIntent)
    }

    fun disableAlarm(ctx: Context, enable: Boolean, requestCode: Int) {

        val manager: AlarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(ctx, SilentModeReceiver::class.java).putExtra("enable", enable)

        manager.cancel(PendingIntent.getBroadcast(ctx,
                requestCode, intent, 0) )
    }

    private fun getCalendar(time: Int): Calendar {

        val calendar = Calendar.getInstance()

        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, time / 60)
        calendar.set(Calendar.MINUTE, time % 60)

        return calendar
    }
}