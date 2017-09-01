package kifio.ringtones

import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.app.job.JobInfo
import android.content.ComponentName
import android.content.Context
import android.app.job.JobScheduler
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat

class MainActivity : AppCompatActivity() {

    private val jobID = 1
    private val requestCode = 1
    private val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_SETTINGS,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val granted = PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)
        pushFragment(SettingsFragment(), null)
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == granted &&
                ActivityCompat.checkSelfPermission(this, permissions[1]) == granted &&
                ActivityCompat.checkSelfPermission(this, permissions[2]) == granted) {
            schedule()
        } else {
            ActivityCompat.requestPermissions(this, permissions, requestCode)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.size == 3 && grantResults[0] == granted && grantResults[1] == granted && grantResults[2] == granted) {
            schedule()
        }
    }

    private fun schedule() {
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(JobInfo.Builder(jobID,
                ComponentName(this, SetRingtoneService::class.java))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(1000 * 60)
                .setPersisted(true)
                .build())
    }

    private fun pushFragment(f: Fragment, title: CharSequence?) {
        setTitle(title)
        supportFragmentManager.beginTransaction()
                .replace(R.id.container, f)
                .addToBackStack(f.javaClass.simpleName)
                .commit()
    }

    override fun onBackPressed() {
        finish()
    }

}
