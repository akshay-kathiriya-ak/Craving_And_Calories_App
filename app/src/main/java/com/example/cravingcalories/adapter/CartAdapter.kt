package com.example.cravingcalories.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cravingcalories.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartAdapter(
    private val context: Context,
    private val cartItems:MutableList<String>,
    private val cartItemPrices:MutableList<String>,
    private val cartDescription:MutableList<String>,
    private var cartImages:MutableList<String>,
    private val cartIngredient: MutableList<String>,


    private val cartQuantity: MutableList<Int>,

) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

        private val auth =FirebaseAuth.getInstance()
    init {
        val database =FirebaseDatabase.getInstance()
        val userId =auth.currentUser?.uid?:""
        val cartItemNumber =cartItems.size

        itemQuantities =IntArray(cartItemNumber){1}
        cartItemsReference =database.reference.child("user").child(userId).child("CartItems")
    }
    companion object{
        private var itemQuantities:IntArray = intArrayOf()
        private lateinit var cartItemsReference:DatabaseReference
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding =CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return cartItems.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
       holder.bind(position)
    }

    fun getUpdatedItemsQuantities(): MutableList<Int> {
val itemQuntity  = mutableListOf<Int>()
        itemQuntity.addAll(cartQuantity)
        return itemQuntity
    }

    inner class CartViewHolder (private val binding:CartItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
           binding.apply {
               val quantity = itemQuantities[position]
               CartItemName.text = cartItems[position]
               cartItemPrice.text =cartItemPrices[position]
               // set image
               val uriString =cartImages[position]
               val uri =Uri.parse(uriString)
               Glide.with(context).load(uri).into(cartImage)

               cartItemQuntity.text = quantity.toString()

               binding.MinusButton.setOnClickListener {
                   decreaseQuantity(position)
               }

               binding.PlusButton.setOnClickListener {
                   increaseQuantity(position)
               }
               binding.deleteButton.setOnClickListener {
                   val itemPosition = adapterPosition
                   if (itemPosition != RecyclerView.NO_POSITION){
                       deleteItem(itemPosition)
                   }

               }



           }
        }
        private fun increaseQuantity(position: Int){
            if (itemQuantities[position]<10){
                itemQuantities[position]++
                cartQuantity[position]= itemQuantities[position]
                binding.cartItemQuntity.text =itemQuantities[position].toString()

            }
        }
        private fun decreaseQuantity(position: Int){
            if (itemQuantities[position]>1){
                itemQuantities[position]--
                cartQuantity[position ] = itemQuantities[position]
                binding.cartItemQuntity.text =itemQuantities[position].toString()

            }
        }
        private fun deleteItem(position: Int){
           val positionRetrieve =position
            getUniqueKeyAtPosition(positionRetrieve){uniqueKey->
                    if (uniqueKey != null){
                        removeItem(position,uniqueKey)
                    }
            }

        }

        private fun removeItem(position: Int, uniqueKey: String) {
            if (uniqueKey != null){
                cartItemsReference.child(uniqueKey).removeValue().addOnSuccessListener {


                    cartDescription.removeAt(position)
                    cartImages.removeAt(position)
                    cartIngredient.removeAt(position)
                    cartItems.removeAt(position)
                    cartItemPrices.removeAt(position)
                    cartQuantity.removeAt(position)



                    // udate itemQuntity
                    itemQuantities = itemQuantities.filterIndexed {index, i -> index!= position}.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position,cartItems.size)
                }
            }
        }

        private fun getUniqueKeyAtPosition(positionRetrieve: Int,onComplete:(String?)->Unit) {
            cartItemsReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey:String?=null
                    snapshot.children.forEachIndexed{
                        index, dataSnapshot ->
                        if (index == positionRetrieve)
                        {
                            uniqueKey =dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }

}