package com.example.cravingcalories.Fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cravingcalories.Model.CartItems
import com.example.cravingcalories.PayOutActivity
import com.example.cravingcalories.adapter.CartAdapter
import com.example.cravingcalories.databinding.FragmentCartBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartFragment : Fragment() {

    private lateinit var auth:FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userId:String
    private lateinit var foodNames:MutableList<String>
    private lateinit var foodPrices:MutableList<String>
    private lateinit var foodDescriptions:MutableList<String>
    private lateinit var foodImagesUri:MutableList<String>
    private lateinit var foodIngredients:MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdapter: CartAdapter

    private lateinit var binding: FragmentCartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()

        retriveCartItems()



        binding.proceedButton.setOnClickListener {
            // get order item details before proceeding to check out
          getOrderItemDetails()
        }


        return binding.root
    }

    private fun getOrderItemDetails() {
        val orderIdReference:DatabaseReference =database.reference.child("user").child(userId).child("CartItems")
        val foodDescription = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodIngredient = mutableListOf<String>()
        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodQuantities =  cartAdapter.getUpdatedItemsQuantities()

        orderIdReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){
                    val orderItems = foodSnapshot.getValue(CartItems::class.java)
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodDescription?.let { foodDescription.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                    orderItems?.foodIngredient?.let { foodIngredient.add(it) }
                    orderItems?.foodQuantity?.let { foodQuantities.add(it) }
                }
                Log.d("CartFragment", "Order details: $foodName, $foodPrice, $foodQuantities")
                orderNow(foodName,foodPrice,foodDescription,foodImage,foodIngredient,foodQuantities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"Failed",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun orderNow(

        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodDescription: MutableList<String>,
        foodImage: MutableList<String>,
        foodIngredient: MutableList<String>,
        foodQuantities: MutableList<Int>
    ){
        if(isAdded && context!=null){
            val intent =Intent(requireContext(),PayOutActivity::class.java).apply {
                putExtra("MenuItemName",foodName as ArrayList<String>)
                putExtra("MenuItemPrice",foodPrice as ArrayList<String>)
                putExtra("MenuItemDescription",foodDescription as ArrayList<String>)
                putExtra("MenuItemImage",foodImage as ArrayList<String>)
                putExtra("MenuItemIngredient",foodIngredient as ArrayList<String>)
                putExtra("MenuItemQuantities",foodQuantities as ArrayList<Int>)
            }
//            intent.putExtra("MenuItemDescription",foodDescription as ArrayList<String>)
//            intent.putExtra("MenuItemImage",foodImage as ArrayList<String>)
//            intent.putExtra("MenuItemIngredient",foodIngredient as ArrayList<String>)
//            intent.putExtra("MenuItemName",foodName as ArrayList<String>)
//            intent.putExtra("MenuItemPrice",foodPrice as ArrayList<String>)
//            intent.putExtra("MenuItemQuantities",foodQuantities as ArrayList<Int>)

            startActivity(intent)
        }



    }


    private fun retriveCartItems() {
        // database refer
        database = FirebaseDatabase.getInstance()
        userId =  auth.currentUser?.uid?:""
        val foodRefernce:DatabaseReference =database.reference.child("user").child(userId).child("CartItems")
        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodDescriptions = mutableListOf()
        foodImagesUri = mutableListOf()
        foodIngredients = mutableListOf()
        quantity = mutableListOf()

        //fetch data to db

        foodRefernce.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in  snapshot.children){
                    val cartItems =foodSnapshot.getValue(CartItems::class.java)
                    cartItems?.foodName?.let { foodNames.add(it) }
                    cartItems?.foodPrice?.let { foodPrices.add(it) }
                    cartItems?.foodDescription?.let { foodDescriptions.add(it) }
                    cartItems?.foodImage?.let { foodImagesUri.add(it) }
                    cartItems?.foodIngredient?.let { foodIngredients.add(it) }
                    cartItems?.foodQuantity?.let { quantity.add(it) }

                }
                setAdapter()
            }

            private fun setAdapter() {
                cartAdapter =CartAdapter(requireContext(),foodNames,foodPrices,foodDescriptions,foodImagesUri,foodIngredients,quantity)
                binding.cartRecylerView.layoutManager =LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                binding.cartRecylerView.adapter = cartAdapter

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    companion object {


    }
}