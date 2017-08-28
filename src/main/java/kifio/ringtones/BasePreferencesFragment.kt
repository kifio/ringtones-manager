package kifio.ringtones

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.Preference.OnPreferenceClickListener

abstract class BasePreferencesFragment : PreferenceFragmentCompat(), OnPreferenceClickListener {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        for (i in 0 until preferenceScreen.preferenceCount)
            preferenceScreen.getPreference(i).onPreferenceClickListener = this
    }

}