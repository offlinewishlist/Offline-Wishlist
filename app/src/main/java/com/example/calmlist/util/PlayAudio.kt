package com.example.calmlist.util

fun playAudio(path: String) {
    val mediaPlayer = android.media.MediaPlayer()
    mediaPlayer.setDataSource(path)
    mediaPlayer.prepare()
    mediaPlayer.start()
}
