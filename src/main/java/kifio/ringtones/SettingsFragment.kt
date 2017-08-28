package kifio.ringtones

import android.app.Notification
import android.app.NotificationManager
import android.os.Bundle
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceFragmentCompat

class SettingsFragment : PreferenceFragmentCompat(), Preference.OnPreferenceClickListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
    }

    override fun onPreferenceClick(p: Preference?): Boolean {
        if (p == null) return false
        val a = activity as MainActivity
        when (p.key) {

            getString(R.string.pref_selected_ringtones) -> {

            }

            getString(R.string.pref_after_each_incoming_call) -> {

            }

            getString(R.string.pref_change_ringtones) -> {

            }
        }
        return true
    }

}