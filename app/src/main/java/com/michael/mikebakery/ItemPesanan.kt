package com.michael.mikebakery
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItemPesanan(
    val namaKue: String = "",
    val kuantitas: Int = 0,
    val hargaSatuan: Double = 0.0
): Parcelable