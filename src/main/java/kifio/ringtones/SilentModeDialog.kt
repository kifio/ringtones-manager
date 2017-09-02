package kifio.ringtones

import android.app.DialogFragment
import android.widget.TextView
import android.view.View
import android.os.Bundle
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.NumberPicker
import android.widget.TimePicker

class SilentModeDialog : DialogFragment() {

    companion object {
        fun newInstance(): SilentModeDialog {
            return SilentModeDialog()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.d_silent_mode_settings, container, false)
        return v
    }
}