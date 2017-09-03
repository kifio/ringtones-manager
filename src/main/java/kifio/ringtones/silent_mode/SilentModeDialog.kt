package kifio.ringtones.silent_mode

import android.os.Build
import android.os.Bundle
import android.support.v7.preference.PreferenceDialogFragmentCompat
import android.text.format.DateFormat
import android.view.View
import android.widget.TimePicker

class SilentModeDialog : PreferenceDialogFragmentCompat() {

    private lateinit var mTimePicker: TimePicker

    companion object {
        fun newInstance(key: String): SilentModeDialog {

            val fragment = SilentModeDialog()
            val b = Bundle(1)

            b.putString(PreferenceDialogFragmentCompat.ARG_KEY, key)
            fragment.arguments = b

            return fragment
        }
    }

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)

        val preference: SilentModePreference = preference as SilentModePreference
        val minutesAfterMidnight = preference.mTime
        val hours = minutesAfterMidnight / 60
        val minutes = minutesAfterMidnight % 60
        val is24hour = DateFormat.is24HourFormat(context)

        mTimePicker = view as TimePicker
        mTimePicker.setIs24HourView(is24hour)
        mTimePicker.currentHour = hours
        mTimePicker.currentMinute = minutes
    }

    override fun onDialogClosed(positiveResult: Boolean) {

        if (positiveResult) {

            val minutesAfterMidnight = getTime()
            val preference: SilentModePreference = preference as SilentModePreference

            if (preference.callChangeListener(minutesAfterMidnight)) {
                preference.mTime = minutesAfterMidnight
            }
        }
    }

    private fun getTime(): Int {

        return if (Build.VERSION.SDK_INT >= 23) {
            mTimePicker.hour * 60 + mTimePicker.minute
        } else {
            mTimePicker.currentHour * 60 + mTimePicker.currentMinute
        }
    }
}