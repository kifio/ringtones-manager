package kifio.ringtones

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Build
import android.preference.PreferenceManager
import android.support.annotation.RequiresApi

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
class JobService : JobService() {

    override fun onStartJob(parameters: JobParameters): Boolean {

        RingtonesManager.resetRingtone(this)
        jobFinished(parameters, false)

        return true
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        return false
    }
}
