package info.nightscout.androidaps.plugins.general.automation.elements

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Typeface
import android.text.format.DateFormat
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import info.nightscout.androidaps.MainApp
import info.nightscout.androidaps.R
import info.nightscout.androidaps.utils.DateUtil
import java.util.*

class InputDateTime(mainApp: MainApp) : Element(mainApp) {
    var value : Long = DateUtil.now()

    constructor(mainApp: MainApp, value : Long) : this(mainApp) {
        this.value = value
    }
    override fun addToLayout(root: LinearLayout) {
        val label = TextView(root.context)
        val dateButton = TextView(root.context)
        val timeButton = TextView(root.context)
        dateButton.text = DateUtil.dateString(value)
        timeButton.text = DateUtil.timeString(value)

        // create an OnDateSetListener
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.timeInMillis = value
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            value = cal.timeInMillis
            dateButton.text = DateUtil.dateString(value)
        }

        dateButton.setOnClickListener {
            root.context?.let {
                val cal = Calendar.getInstance()
                cal.timeInMillis = value
                DatePickerDialog(it, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }

        // create an OnTimeSetListener
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            val cal = Calendar.getInstance()
            cal.timeInMillis = value
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            cal.set(Calendar.SECOND, 0) // randomize seconds to prevent creating record of the same time, if user choose time manually
            value = cal.timeInMillis
            timeButton.text = DateUtil.timeString(value)
        }

        timeButton.setOnClickListener {
            root.context?.let {
                val cal = Calendar.getInstance()
                cal.timeInMillis = value
                TimePickerDialog(it, timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY),
                    cal.get(Calendar.MINUTE),
                    DateFormat.is24HourFormat(mainApp)
                ).show()
            }
        }

        val px = resourceHelper.dpToPx(10)
        label.text = resourceHelper.gs(R.string.atspecifiedtime, "")
        label.setTypeface(label.typeface, Typeface.BOLD)
        label.setPadding(px, px, px, px)
        dateButton.setPadding(px, px, px, px)
        timeButton.setPadding(px, px, px, px)
        val l = LinearLayout(root.context)
        l.orientation = LinearLayout.HORIZONTAL
        l.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        l.addView(label)
        l.addView(dateButton)
        l.addView(timeButton)
        root.addView(l)
    }

}