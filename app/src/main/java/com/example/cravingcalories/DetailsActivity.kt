package com.example.cravingcalories

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.cravingcalories.Model.CartItems
import com.example.cravingcalories.databinding.ActivityDetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    private var foodName:String ?= null
    private var  foodPrice:String?=null
    private var foodDescription:String?=null
    private var foodImage:String?=null
    private var foodIngredient:String?=null
    private lateinit var  binding :ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        foodName =intent.getStringExtra("MenuItemName")
        foodPrice =intent.getStringExtra("MenuItemPrice")
        foodDescription =intent.getStringExtra("MenuItemDescription")
        foodImage =intent.getStringExtra("MenuItemImage")
        foodIngredient =intent.getStringExtra("MenuItemIngredients")



        with(binding){
            DetailFoodNAme.text =foodName
            DescriptionTextView.text =foodDescription
            DetailIngredients.text =foodIngredient
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(DetailFoodImage)
        }
       binding.AddToCartButton.setOnClickListener {
           addItemToCart()
       }
        }

    private fun addItemToCart() {
        val database =FirebaseDatabase.getInstance().reference
        val userId =auth.currentUser?.uid?:""


        val cartItem=CartItems(foodName.toString(),foodPrice.toString(),foodDescription.toString(),foodImage.toString(),foodIngredient.toString(),1)

        database.child("user").child(userId).child("CartItems").push().setValue(cartItem).addOnSuccessListener {
            Toast.makeText(this,"Item Add to Cart",Toast.LENGTH_SHORT).show()
        }
    }

}
