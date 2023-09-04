package com.example.playlistmaker.ui.player

sealed class AvailabilityInfo {
    class AvailabilityData (val isInPlaylist: Boolean, val playlistName: String): AvailabilityInfo()
    object NoNotification: AvailabilityInfo()
}