package com.example.expensetracker.android

import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    fun formatDatetToHumanReadableFormat(dateInMillis:Long) : String{
        val dateFormatter= SimpleDateFormat("DD/MM/YYYY", Locale.getDefault())
        return dateFormatter.format(dateInMillis)
    }
    fun formatToDecimalValue(d: Double):String{
        return String.format("% 2f, d")
    }
}