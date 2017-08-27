package kifio.ringtones

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat

class MainPreferencesFragment : PreferenceFragmentCompat(),
        android.support.v7.preference.Preference.OnPreferenceClickListener {

    override fun onPreferenceClick(p: android.support.v7.preference.Preference?): Boolean {
        if (p == null) return false
        val a = activity as MainActivity
        if (p.key == "how_to_change") {
            a.pushFragment(ChangeRingtonePreferencesFragment(), p.title)
        }
        return true
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings)
        for (i in 0 until preferenceScreen.preferenceCount)
            preferenceScreen.getPreference(i).onPreferenceClickListener = this
    }

}