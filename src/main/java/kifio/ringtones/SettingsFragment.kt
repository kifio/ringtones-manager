package kifio.ringtones

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.preference.Preference
import android.support.v7.preference.Preference.OnPreferenceChangeListener
import android.support.v7.preference.PreferenceFragmentCompat
import kifio.ringtones.silent_mode.SilentModeAlarm
import kifio.ringtones.silent_mode.SilentModeDialog
import kifio.ringtones.silent_mode.SilentModePreference

class SettingsFragment : PreferenceFragmentCompat(), OnPreferenceChangeListener {

    private lateinit var setImmediately: Preference
    private lateinit var enableSilentMode: Preference
    private lateinit var from: SilentModePreference
    private lateinit var to: SilentModePreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)

        val keyFrom = getString(R.string.pref_silent_mode_from)
        val keyTo = getString(R.string.pref_silent_mode_to)

        setImmediately = preferenceScreen.findPreference(getString(R.string.pref_change_immediately))
        enableSilentMode  = preferenceScreen.findPreference(getString(R.string.pref_silent_mode_enable))
        from = preferenceScreen.findPreference(keyFrom) as SilentModePreference
        to = preferenceScreen.findPreference(keyTo) as SilentModePreference

        setImmediately.setOnPreferenceClickListener { resetRingtone() }
        enableSilentMode.onPreferenceChangeListener = this
        from.onPreferenceChangeListener = this
        to.onPreferenceChangeListener = this
    }

    private fun resetRingtone(): Boolean {

        activity.startService(Intent(activity, ResetRingtoneService::class.java))

        return true
    }

    override fun onPreferenceChange(preference: Preference?, value: Any): Boolean {

        if ((value is Boolean && value) || value is Int) {
            SilentModeAlarm.scheduleVibrationMode(activity, from.mTime, to.mTime)
        }

        return true
    }

    override fun onDisplayPreferenceDialog(preference: Preference) {

        val dialogFragment: DialogFragment = SilentModeDialog.newInstance(preference.key)

        dialogFragment.setTargetFragment(this, 0)
        dialogFragment.show(activity.supportFragmentManager, SilentModeDialog::class.java.name)
    }

}