package com.michael.mikebakery

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.mikebakery.databinding.ActivityAddKueBinding

class AddKueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddKueBinding

    private val staticImageUrl = "https://media.istockphoto.com/id/924845956/id/vektor/sepotong-kue-cokelat.jpg?s=612x612&w=0&k=20&c=E4u168YxwYxv1R0Vp2ollMQyXEMiajPX1dC6qHVfKeI="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddKueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonSaveCake.setOnClickListener {
            val name = binding.editTextCakeName.text.toString().trim()
            val price = binding.editTextCakePrice.text.toString().trim()
            val description = binding.editTextCakeDescription.text.toString().trim()

            if (name.isEmpty() || price.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveDataToFirestore(name, price.toDouble(), description)
        }
    }

    private fun saveDataToFirestore(name: String, price: Double, description: String) {
        binding.progressBar.visibility = View.VISIBLE
        val firestore = FirebaseFirestore.getInstance()

        val kue = hashMapOf(
            "nama" to name,
            "harga" to price,
            "deskripsi" to description,
            "imageUrl" to staticImageUrl
        )

        firestore.collection("kue")
            .add(kue)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Kue berhasil ditambahkan!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}