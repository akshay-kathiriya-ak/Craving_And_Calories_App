package com.example.cravingcalories

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.cravingcalories.databinding.ActivityStartBinding
import com.google.firebase.auth.FirebaseAuth


class StartActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private val binding :ActivityStartBinding by lazy {
        ActivityStartBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth =FirebaseAuth.getInstance()
        binding.startButton.setOnClickListener {
            val intent =Intent(this,LoginActivity::class.java)
            intent.putExtra("action","login")
            startActivity(intent)
            finish()

        }

    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}