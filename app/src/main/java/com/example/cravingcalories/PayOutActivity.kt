package com.example.cravingcalories

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.cravingcalories.Model.OrderDetails
import com.example.cravingcalories.databinding.ActivityPayOutBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {
    private lateinit var auth:  FirebaseAuth
    private lateinit var name:String
    private lateinit var address:String
    private lateinit var phone:String
    private lateinit var totalAmount:String
    private lateinit var foodItemName:ArrayList<String>
    private lateinit var foodItemPrice:ArrayList<String>
    private lateinit var foodItemImage:ArrayList<String>
    private lateinit var foodItemDescription:ArrayList<String>
    private lateinit var foodItemIngredient:ArrayList<String>
    private lateinit var foodItemQuantities:ArrayList<Int>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId:String

    private lateinit var binding:ActivityPayOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =ActivityPayOutBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        auth=FirebaseAuth.getInstance()
        databaseReference =FirebaseDatabase.getInstance().getReference()
        setUserData()


        foodItemName = intent.getStringArrayListExtra("MenuItemName") ?: arrayListOf()
        foodItemPrice = intent.getStringArrayListExtra("MenuItemPrice")?: arrayListOf()
        foodItemImage = intent.getStringArrayListExtra("MenuItemImage") ?: arrayListOf()
        foodItemDescription = intent.getStringArrayListExtra("MenuItemDescription") ?: arrayListOf()
        foodItemIngredient = intent.getStringArrayListExtra("MenuItemIngredient") ?: arrayListOf()
        foodItemQuantities = intent.getIntegerArrayListExtra("MenuItemQuantities") ?: arrayListOf()
        Log.d("PayOutActivity", "Received Data - Description: $foodItemDescription, Images: $foodItemImage, Ingredients: $foodItemIngredient, Names: $foodItemName, Prices: $foodItemPrice, Quantities: $foodItemQuantities")

        totalAmount = calculateTotalAmount().toString()
        Log.d("PayOutActivity", "Calculated Total Amount: $totalAmount")
        binding.totalRs.setText(totalAmount)

        binding.placeorderbutton.setOnClickListener {
            name =binding.Nameofuser.text.toString().trim()
            address=binding.Addressofuser.text.toString().trim()
            phone=binding.phonenoofuser.text.toString().trim()
            if (name.isBlank() && address.isBlank() &&phone.isBlank()){
                Toast.makeText(this,"Please Enter All The Details",Toast.LENGTH_SHORT).show()
            }else{
                placeOrder()
            }



        }
    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid?:""
        val time =System.currentTimeMillis()
        val itemPushKey =databaseReference.child("OrderDetails").push().key
        val orderDetails =OrderDetails(userId,name,foodItemName,foodItemPrice,foodItemImage,foodItemQuantities,address,totalAmount,phone,time,itemPushKey,false,false)
        val orderReference =databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {
            removeItemFromCart()
            addOrderToHistory(orderDetails)
            val bottomSheetDialog = CongratsBottomSheet()
            bottomSheetDialog.show(supportFragmentManager,"Test")
        }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails):  Void?{
databaseReference.child("user").child(userId).child("BuyHistory")
    .child(orderDetails.itemPushKey!!)
    .setValue(orderDetails).addOnSuccessListener {
                 Toast.makeText(this,"Item Add history",Toast.LENGTH_SHORT).show()
    }

        return null
    }

    private fun removeItemFromCart() {
        val cartItemReference = databaseReference.child("user").child(userId).child("CartItems")
        cartItemReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalAmount = 0
        for (i in foodItemPrice.indices) {
            val price = foodItemPrice[i]
            // Remove any non-numeric characters (like $) at the end
            val priceIntValue = price.filter { it.isDigit() }.toIntOrNull() ?: 0
            val quantity = foodItemQuantities[i]
            totalAmount += priceIntValue * quantity
        }
        return totalAmount
    }

    private fun setUserData() {
        val user =auth.currentUser
        if (user != null){
            val userId = user.uid
            val userReference =databaseReference.child("user").child(userId)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val names =snapshot.child("name").getValue(String::class.java)?:""
                        val addresss = snapshot.child("address").getValue(String::class.java)?:""
                        val phones = snapshot.child("phone").getValue(String::class.java)?:""
                        binding.apply {
                            Nameofuser.setText(names)
                            Addressofuser.setText(addresss)
                            phonenoofuser.setText(phones)
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }
    }
}