package com.michael.mikebakery

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.michael.mikebakery.databinding.ActivityLihatPesananBinding

class LihatPesananActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLihatPesananBinding
    private lateinit var pesananAdapter: PesananAdapter
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLihatPesananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pesananAdapter = PesananAdapter(emptyList())
        binding.recyclerViewOrders.adapter = pesananAdapter

        fetchPesananData()
    }

    private fun fetchPesananData() {
        binding.progressBar.visibility = View.VISIBLE

        firestore.collection("pesanan")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                binding.progressBar.visibility = View.GONE

                val pesananList = result.documents.mapNotNull { document ->
                    val pesanan = document.toObject(Pesanan::class.java)
                    pesanan?.id = document.id
                    pesanan
                }

                pesananAdapter = PesananAdapter(pesananList)
                binding.recyclerViewOrders.adapter = pesananAdapter
            }
            .addOnFailureListener { exception ->
                binding.progressBar.visibility = View.GONE
                Log.e("LihatPesanan", "Error fetching data", exception)
                Toast.makeText(this, "Gagal mengambil data pesanan.", Toast.LENGTH_SHORT).show()
            }
    }
}