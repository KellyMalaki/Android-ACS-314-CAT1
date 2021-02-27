package com.example.cat1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cat1.databinding.ActivityLoginBinding
import com.example.cat1.databinding.ActivityMainBinding
import com.example.cat1.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnsignuplogin.setOnClickListener {
            val enterlogin = Intent (applicationContext, LoginActivity::class.java)
            startActivity(enterlogin)
        }

        binding.btnsignupsignup.setOnClickListener {
            val entermain = Intent (applicationContext, MainActivity::class.java)
            startActivity(entermain)
        }
    }
}