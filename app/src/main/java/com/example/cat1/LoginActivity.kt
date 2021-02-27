package com.example.cat1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cat1.databinding.ActivityLoginBinding
import com.example.cat1.databinding.ActivityMainBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.btnLoginLogin.setOnClickListener {
            val enterlogin = Intent (applicationContext, MainActivity::class.java)
            startActivity(enterlogin)
        }

        binding.btnLoginSignUp.setOnClickListener {
            val enter_signup = Intent (applicationContext, SignUpActivity::class.java)
            startActivity(enter_signup)
        }
    }
}