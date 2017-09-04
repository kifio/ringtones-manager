package kifio.ringtones

import android.app.AlarmManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val JOB_ID = 210
    private val permRequestCode = 199
    private val settingRequestCode = 200
    private val permissions = arrayOf(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private val granted = PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.a_main)
        pushFragment(SettingsFragment(), null)
        checkWriteSettingsPermission()
    }

    private fun checkWriteSettingsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(applicationContext)) {

                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + packageName))

                startActivityForResult(intent, settingRequestCode)
            } else {
                checkPermissions()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (grantResults.size == permissions.size && grantResults.none { grantResults[it] != granted }) {
            schedule()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == settingRequestCode) {
            checkWriteSettingsPermission()
        }
    }

    private fun checkPermissions() {
        if (ActivityCompat.checkSelfPermission(this, permissions[0]) == granted &&
                ActivityCompat.checkSelfPermission(this, permissions[1]) == granted) {
            schedule()
        } else {
            ActivityCompat.requestPermissions(this, permissions, permRequestCode)
        }
    }

    fun schedule() {
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(JobInfo.Builder(JOB_ID,
                ComponentName(this, JobService::class.java))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(AlarmManager.INTERVAL_DAY)
                .setPersisted(true)
                .build())
    }

    fun cancel() {
        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.cancel(JOB_ID)
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
