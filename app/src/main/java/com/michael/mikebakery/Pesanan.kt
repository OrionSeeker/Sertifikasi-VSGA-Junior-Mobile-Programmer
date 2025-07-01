package com.michael.mikebakery

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pesanan(
    var id: String = "",
    val namaPelanggan: String = "",
    val totalHarga: Double = 0.0,
    val items: List<ItemPesanan> = emptyList(),
    @ServerTimestamp
    val timestamp: Date? = null,
    val lokasi: Lokasi? = null
): Parcelable