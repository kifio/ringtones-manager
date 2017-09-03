package kifio.ringtones

import android.app.IntentService
import android.content.Intent

class ResetRingtoneService : IntentService("ResetRingtoneService") {

    override fun onHandleIntent(intent: Intent) {
        RingtonesManager.resetRingtone(this)
    }

}