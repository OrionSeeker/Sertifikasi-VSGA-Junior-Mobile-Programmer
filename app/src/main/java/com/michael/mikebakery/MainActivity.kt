package com.michael.mikebakery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.michael.mikebakery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var kueAdapter: KueAdapter
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val loggedInUser = getLoggedInUser()

        when (loggedInUser) {
            null -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                return
            }
            "admin" -> {
                val intent = Intent(this, AdminMenuActivity::class.java)
                startActivity(intent)
                finish()
                return
            }
            else -> {

            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        kueAdapter = KueAdapter(emptyList())
        binding.recyclerViewKue.adapter = kueAdapter

        fetchKueData()

        binding.buttonLogout.setOnClickListener {
            clearLoginSession()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.buttonToCart.setOnClickListener {
            val intent = Intent(this, KeranjangActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchKueData() {
        binding.progressBar.visibility = View.VISIBLE
        firestore.collection("kue")
            .get()
            .addOnSuccessListener { result ->
                binding.progressBar.visibility = View.GONE
                val kueList = result.documents.mapNotNull { document ->
                    val kue = document.toObject(Kue::class.java)
                    kue?.id = document.id
                    kue
                }
                kueAdapter.updateData(kueList)
            }
            .addOnFailureListener { exception ->
                binding.progressBar.visibility = View.GONE
                Log.e("MainActivity", "Error fetching kue data", exception)
                Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun isUserLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("login_session", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isLoggedIn", false)
    }

    private fun clearLoginSession() {
        val sharedPref = getSharedPreferences("login_session", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
    }

    private fun getLoggedInUser(): String? {
        val sharedPref = getSharedPreferences("login_session", Context.MODE_PRIVATE)
        return if (sharedPref.getBoolean("isLoggedIn", false)) {
            sharedPref.getString("username", null)
        }
        else {
            null
        }
    }

}