package com.michael.mikebakery

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.mikebakery.databinding.ActivityKeranjangBinding
import java.text.NumberFormat
import java.util.Locale

class KeranjangActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKeranjangBinding
    private lateinit var keranjangAdapter: KeranjangAdapter
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKeranjangBinding.inflate(layoutInflater)
        setContentView(binding.root)

        keranjangAdapter = KeranjangAdapter(emptyList())
        binding.recyclerViewKeranjang.adapter = keranjangAdapter

        binding.buttonCheckout.setOnClickListener {
            if (Keranjang.items.isNotEmpty()) {
                val intent = Intent(this, CheckoutActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Keranjang Anda kosong", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        updateCartView()
    }

    private fun updateCartView() {
        val itemsInCart = Keranjang.items
        keranjangAdapter.updateData(itemsInCart)

        val totalHarga = Keranjang.getTotalPrice()
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        binding.textViewTotalHarga.text = formatter.format(totalHarga)
    }

    private fun prosesCheckout() {
        if (Keranjang.items.isEmpty()) {
            Toast.makeText(this, "Keranjang Anda kosong", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        val pesananData = hashMapOf(
            "userId" to "user_123",
            "totalHarga" to Keranjang.getTotalPrice(),
            "timestamp" to FieldValue.serverTimestamp(),
            "items" to Keranjang.items.map { keranjangItem ->
                mapOf(
                    "idKue" to keranjangItem.kue.id,
                    "namaKue" to keranjangItem.kue.nama,
                    "hargaSatuan" to keranjangItem.kue.harga,
                    "kuantitas" to keranjangItem.kuantitas
                )
            }
        )

        firestore.collection("pesanan")
            .add(pesananData)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Checkout berhasil! Pesanan sedang diproses.", Toast.LENGTH_LONG).show()

                Keranjang.clearCart()
                finish()
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                Log.e("CheckoutError", "Gagal menyimpan pesanan", e)
                Toast.makeText(this, "Checkout gagal: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}