package com.example.cravingcalories

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.cravingcalories.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var email:String
    private lateinit var password:String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private val binding:ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth= Firebase.auth
        database =FirebaseDatabase.getInstance()
        setContentView(binding.root)
        binding.Signupbutton.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        val action=intent.getStringExtra("action")
if(action == "login"){
    binding.loginButton.setOnClickListener {
        email =binding.email.text.toString().trim()
        password =binding.password.text.toString().trim()
        if(email.isBlank()||password.isBlank()){
            Toast.makeText(this,"Please Fill All Details", Toast.LENGTH_SHORT).show()
        }else {
            auth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener {
                        task->if (task.isSuccessful){

                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this,"Login Successful", Toast.LENGTH_SHORT).show()
                    finish()
                }
                }.addOnFailureListener{
                    Toast.makeText(this,"Login Failed", Toast.LENGTH_SHORT).show()
                }
        }
    }
}



    }
}