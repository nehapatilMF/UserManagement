package com.example.usermanagementapp.utils
import android.app.DatePickerDialog
import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {

    fun showDatePicker(context: Context, listener: DatePickerDialog.OnDateSetListener) {
        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            listener.onDateSet(null, year, monthOfYear, dayOfMonth)
        }

        DatePickerDialog(
            context,
            dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun getCurrentDate(): String {
        val cal = Calendar.getInstance()
        val myFormat = "dd.MM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        return sdf.format(cal.time)
    }
}
