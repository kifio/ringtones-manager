package kifio.ringtones

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat

/**
 * Created by kifio on 27.08.17.
 */
class ChangeRingtonePreferencesFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.change_ringtone)
    }

}