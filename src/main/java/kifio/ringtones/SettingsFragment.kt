package kifio.ringtones

import android.content.Intent
import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.Preference.OnPreferenceChangeListener
import android.support.v7.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat(), OnPreferenceChangeListener {

    private lateinit var keyChangeRingtoneSchedule: String
    private lateinit var keyChangeRingtoneImmediately: String

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)

        keyChangeRingtoneSchedule = getString(R.string.pref_change_by_schedule)
        keyChangeRingtoneImmediately = getString(R.string.pref_change_immediately)

        val changeBySchedule = preferenceScreen.findPreference(keyChangeRingtoneSchedule)
        val setImmediately = preferenceScreen.findPreference(keyChangeRingtoneImmediately)


        setImmediately.setOnPreferenceClickListener { resetRingtone() }
        changeBySchedule.onPreferenceChangeListener = this

    }

    override fun onPreferenceChange(preference: Preference, value: Any): Boolean {

        scheduleRingtonesChanging(value as Boolean)

        return true
    }

    private fun resetRingtone(): Boolean {

        activity.startService(Intent(activity, ResetRingtoneService::class.java))

        return true
    }

    private fun scheduleRingtonesChanging(enable: Boolean): Boolean {

        val a: MainActivity = activity as MainActivity

        if (enable) a.schedule() else a.cancel()

        return true
    }

}