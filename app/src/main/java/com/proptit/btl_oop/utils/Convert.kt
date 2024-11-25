package com.proptit.btl_oop.utils

import java.text.SimpleDateFormat
import java.util.Date

object Convert {
    fun longToDateTime(time: Long): String {
        val dateFormat = SimpleDateFormat("HH:mm - dd/MM/yyyy")
        val date = Date(time)
        return dateFormat.format(date)
    }
}