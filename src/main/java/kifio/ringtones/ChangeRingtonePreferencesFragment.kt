package kifio.ringtones

import android.os.Bundle
import android.support.v7.preference.Preference

class ChangeRingtonePreferencesFragment : BasePreferencesFragment() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.how_to_change)
        for (i in 0 until preferenceScreen.preferenceCount)
            preferenceScreen.getPreference(i).onPreferenceClickListener = this
    }

    override fun onPreferenceClick(p: Preference?): Boolean {
        if (p == null) return false
        if (p.key == "after_incoming_calls") {
            NumberPickerFragment().show(activity.fragmentManager, "number_picker")
        }
        return true
    }

}