package com.michael.mikebakery

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.michael.mikebakery.databinding.ActivityAdminMenuBinding

class AdminMenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonAddCake.setOnClickListener {
            val intent = Intent(this, AddKueActivity::class.java)
            startActivity(intent)
        }

        binding.buttonViewOrders.setOnClickListener {
            val intent = Intent(this, LihatPesananActivity::class.java)
            startActivity(intent)
        }

        binding.buttonAdminLogout.setOnClickListener {
            clearLoginSession()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun clearLoginSession() {
        val sharedPref = getSharedPreferences("login_session", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
    }
}