package com.michael.mikebakery

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Lokasi(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) : Parcelable