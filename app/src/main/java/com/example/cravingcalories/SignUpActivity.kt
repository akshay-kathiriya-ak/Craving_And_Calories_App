package com.example.cravingcalories

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.cravingcalories.Model.UserModel
import com.example.cravingcalories.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var email:String
    private lateinit var password:String
    private lateinit var userName:String
    private lateinit var auth:FirebaseAuth
    private lateinit var database: DatabaseReference
    private val binding :ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth = Firebase.auth
        database =Firebase.database.reference
        binding.SignInbutton.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }

        binding.createAccountbutton.setOnClickListener{
            userName =binding.userNameTextView.text.toString()
            email= binding.emailTextView.text.toString().trim()
            password =binding.passwordNameTextView.text.toString().trim()
            if(email.isEmpty() ||userName.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Please Fill All Details",Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }
        }




    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
            task->if (task.isSuccessful){
                Toast.makeText(this,"Account Create Successfully",Toast.LENGTH_SHORT).show()
            saveUserData()
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
            }else{
            Toast.makeText(this,"Account Create Fail",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveUserData() {
        userName =binding.userNameTextView.text.toString()
        email=binding.emailTextView.text.toString().trim()
        password=binding.passwordNameTextView.text.toString().trim()

        val user =UserModel(userName,email,password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)

    }
}