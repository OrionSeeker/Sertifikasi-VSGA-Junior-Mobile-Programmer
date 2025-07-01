package com.michael.mikebakery

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.michael.mikebakery.databinding.ActivityDetailPesananBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

class DetailPesananActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailPesananBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pesanan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("EXTRA_PESANAN", Pesanan::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("EXTRA_PESANAN")
        }

        if (pesanan != null) {
            bindDataToViews(pesanan)
        }
    }

    private fun bindDataToViews(pesanan: Pesanan) {
        val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        val dateFormatter = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("in", "ID"))

        binding.textViewIdPesanan.text = "ID: ${pesanan.id}"
        binding.textViewNamaPelangganDetail.text = pesanan.namaPelanggan
        binding.textViewTotalHargaDetail.text = currencyFormatter.format(pesanan.totalHarga)

        pesanan.timestamp?.let {
            binding.textViewTanggalDetail.text = dateFormatter.format(it)
        }

        val rincianText = StringBuilder()
        pesanan.items.forEach { item ->
            rincianText.append("- ${item.namaKue} x ${item.kuantitas}\n")
        }
        binding.textViewRincianItem.text = rincianText.toString()

        pesanan.lokasi?.let {
            val koordinat = "${it.latitude}, ${it.longitude}"
            binding.textViewLokasiDetail.text = koordinat
        }
    }
}