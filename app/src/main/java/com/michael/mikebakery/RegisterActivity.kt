package com.michael.mikebakery

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.michael.mikebakery.databinding.ActivityRegisterBinding
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDao = AppDatabase.getDatabase(this).userDao()

        binding.buttonRegister.setOnClickListener {
            val username = binding.editTextUsernameRegister.text.toString().trim()
            val password = binding.editTextPasswordRegister.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    val existingUser = userDao.findUserByUsername(username)
                    if (existingUser == null) {
                        userDao.registerUser(User(username = username, password = password))
                        Toast.makeText(this@RegisterActivity, "Registrasi berhasil!", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Username sudah digunakan!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Username dan/atau password tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textViewToLogin.setOnClickListener {
            finish()
        }
    }
}