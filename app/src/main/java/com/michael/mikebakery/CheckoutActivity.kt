package com.michael.mikebakery

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.mikebakery.databinding.ActivityCheckoutBinding

class CheckoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private val firestore = FirebaseFirestore.getInstance()
    private var lokasiPengiriman: Location? = null

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Izin lokasi di tolakkk", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAmbilLokasi.setOnClickListener {
            checkLocationPermission()
        }

        binding.buttonKonfirmasiPesanan.setOnClickListener {
            prosesKonfirmasiPesanan()
        }
    }

    private fun checkLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                getCurrentLocation()
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    this.lokasiPengiriman = location
                    binding.textViewLokasi.text = "Lokasi: ${location.latitude}, ${location.longitude}"
                    Toast.makeText(this, "Lokasi berhasil didapatkan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Tidak bisa mendapatkan lokasi. Pastikan GPS aktif.", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun prosesKonfirmasiPesanan() {
        val nama = binding.editTextNamaPelanggan.text.toString().trim()
        val telepon = binding.editTextNomorTelepon.text.toString().trim()

        if (nama.isEmpty() || telepon.isEmpty() || lokasiPengiriman == null) {
            Toast.makeText(this, "Nama, telepon, dan lokasi wajib diisi!", Toast.LENGTH_SHORT).show()
            return
        }

        if (Keranjang.items.isEmpty()) {
            Toast.makeText(this, "Keranjang Anda kosong", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBarCheckout.visibility = View.VISIBLE

        val pesananData = hashMapOf(
            "namaPelanggan" to nama,
            "nomorTelepon" to telepon,
            "totalHarga" to Keranjang.getTotalPrice(),
            "timestamp" to FieldValue.serverTimestamp(),
            "lokasi" to hashMapOf(
                "latitude" to lokasiPengiriman!!.latitude,
                "longitude" to lokasiPengiriman!!.longitude
            ),
            "items" to Keranjang.items.map { mapOf(
                "idKue" to it.kue.id,
                "namaKue" to it.kue.nama,
                "hargaSatuan" to it.kue.harga,
                "kuantitas" to it.kuantitas
            )}
        )

        firestore.collection("pesanan")
            .add(pesananData)
            .addOnSuccessListener {
                binding.progressBarCheckout.visibility = View.GONE
                Toast.makeText(this, "Pesanan berhasil dibuat!", Toast.LENGTH_LONG).show()

                Keranjang.clearCart()

                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                binding.progressBarCheckout.visibility = View.GONE
                Log.e("CheckoutError", "Gagal menyimpan pesanan", e)
                Toast.makeText(this, "Pesanan gagal: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}