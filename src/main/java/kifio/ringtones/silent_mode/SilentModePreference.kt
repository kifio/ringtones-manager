package kifio.ringtones.silent_mode

import android.content.Context
import android.content.res.TypedArray
import android.support.v7.preference.DialogPreference
import android.util.AttributeSet
import kifio.ringtones.R

class SilentModePreference(context: Context?, attrs: AttributeSet) : DialogPreference(context, attrs) {

    var mTime: Int = 0
        set(value) {
            persistInt(value)
            field = value
            summary = String.format("%02d:%02d", value / 60, value % 60)
        }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getInt(index, 0)
    }

    override fun onSetInitialValue(restorePersistedValue: Boolean, defaultValue: Any?) {
        mTime = if (restorePersistedValue) getPersistedInt(mTime) else defaultValue as Int
    }

    override fun getDialogLayoutResource(): Int {
        return R.layout.d_silent_mode_settings
    }

}