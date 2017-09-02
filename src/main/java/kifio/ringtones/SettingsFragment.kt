package kifio.ringtones

import android.os.Bundle
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        for(i in 0 until preferenceScreen.preferenceCount) {
            preferenceScreen.getPreference(i).onPreferenceClickListener = this
        }
    }

    override fun onPreferenceClick(p: Preference?): Boolean {
        if (p == null) return false
        when (p.key) {

            getString(R.string.pref_change_by_schedule) -> {

            }

            getString(R.string.pref_change_immediately) -> {
                RingtonesManager.resetRingtone(activity)
            }

            getString(R.string.pref_silent_mode_enable) -> {
                SilentModeDialog.newInstance().show(activity.fragmentManager, "asdf")
            }
        }
        return true
    }

}