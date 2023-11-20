package com.example.musicapp


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Songs(val title: String?, val artist: String?, val path: String?) : Parcelable
