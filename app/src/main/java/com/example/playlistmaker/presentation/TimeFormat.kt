package com.example.playlistmaker.presentation

import java.text.SimpleDateFormat
import java.util.Locale

object TimeFormat {
    fun formatTime (time: Long) : String {
        return SimpleDateFormat(
            "mm:ss",
            Locale.getDefault()
        ).format(time)
    }
}