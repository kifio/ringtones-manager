package kifio.ringtones

import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.NumberPicker

class NumberPickerFragment : DialogFragment(), DialogInterface.OnClickListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setView(getNumberPickerView())
                .setPositiveButton(android.R.string.ok, this)
                .create()
    }

    private fun getNumberPickerView(): NumberPicker {
        val view = NumberPicker(activity)
        view.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        return view
    }

    // TODO: Перезаписывать SharedPreference с количеством звонков, после которого надо менять рингтон
    override fun onClick(d: DialogInterface?, order: Int) {
        dismiss()
    }
}