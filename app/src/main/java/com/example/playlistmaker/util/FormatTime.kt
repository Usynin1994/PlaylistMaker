package com.example.playlistmaker.util

import android.icu.text.SimpleDateFormat
import java.util.Locale


fun Long.formatAsTime(): String {
    return SimpleDateFormat("mm:ss", Locale.getDefault()).format(this)
}

fun Long.formatAsMinutes(): Long {
    return this/60000
}