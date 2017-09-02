package kifio.ringtones

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.preference.PreferenceManager
import android.support.annotation.RequiresApi

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class SetRingtoneService : JobService() {

    override fun onStartJob(parameters: JobParameters): Boolean {
        val sp = PreferenceManager.getDefaultSharedPreferences(this)
        if (sp.getBoolean(getString(R.string.pref_change_by_schedule), false)) {
            RingtonesManager.resetRingtone(this)
        }
        jobFinished(parameters, false)
        return true
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        return false
    }
}
