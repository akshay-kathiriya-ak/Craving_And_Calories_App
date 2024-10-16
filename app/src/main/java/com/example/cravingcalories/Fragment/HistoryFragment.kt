package com.example.cravingcalories.Fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cravingcalories.Model.OrderDetails
import com.example.cravingcalories.adapter.BuyAgainAdapter
import com.example.cravingcalories.databinding.FragmentHistoryBinding
import com.example.cravingcalories.recentOrderItemsActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HistoryFragment : Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId:String
    private  var listOfOrederList:ArrayList<OrderDetails> = arrayListOf()
    private lateinit var binding:FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth= FirebaseAuth.getInstance()
        database =FirebaseDatabase.getInstance()
        // Inflate the layout for this fragment
        binding =FragmentHistoryBinding.inflate(layoutInflater,container,false)
        // retrive and Display user order hiiistory
        retrieveBuyHistory()
        binding.recentBuyItem.setOnClickListener{
            seeItemRecentBuy()
        }
        binding.receviedbutton.setOnClickListener {
            updateOrderStatus()
        }

        return binding.root
    }

    private fun updateOrderStatus() {
        val itemPushKey =listOfOrederList[0].itemPushKey
        val completeOrderRefernce = database.reference.child("CompletedOrder").child(itemPushKey!!)
        completeOrderRefernce.child("paymentReceived").setValue(true)
    }

    private fun seeItemRecentBuy() {
        listOfOrederList.firstOrNull()?.let { recentBuy->
            val intent =Intent(requireContext(),recentOrderItemsActivity::class.java)
            intent.putExtra("RecentBuyOrderItem",listOfOrederList)
            startActivity(intent)

        }
    }

    private fun retrieveBuyHistory() {
        binding.recentBuyItem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid?:""
        val buyItemReference:DatabaseReference =database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery = buyItemReference.orderByChild("currentTime")
        shortingQuery.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (buySnapshot in snapshot.children){
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrederList.add(it)
                    }
                }
                listOfOrederList.reverse()
                if (listOfOrederList.isNotEmpty()){
                    setDataInRecentBuyItem()
                    setPreviousBuyItemRecyclerView()
                }
            }

            private fun setDataInRecentBuyItem() {
                binding.recentBuyItem.visibility =View.VISIBLE
                val recentOrderItem =listOfOrederList.firstOrNull()
                recentOrderItem?.let {
                    with(binding){
                         BuyAgainFoodName.text =it.foodNames?.firstOrNull()?:""
                        BuyAgainFoodPrice.text =it.foodPrice?.firstOrNull()?:""
                        val image =it.foodImages?.firstOrNull()?:""
                        val uri =Uri.parse(image)
                        Glide.with(requireContext()).load(uri).into(BuyAgainFoodImage)
                        val isOrderIsAccepted =listOfOrederList[0].orderAccepted
                        if (isOrderIsAccepted) {
                            orderStatus.background.setTint(Color.GREEN)
                            receviedbutton.visibility = View.VISIBLE
                        }

                    }
                }
            }

            private fun setPreviousBuyItemRecyclerView() {
                val buyAgainFoodName = mutableListOf<String>()
                val buyAgainFoodPrice =  mutableListOf<String>()
                val buyAgainFoodImage = mutableListOf<String>()
                for (i in 1 until listOfOrederList.size){
                    listOfOrederList[i].foodNames?.firstOrNull()?.let {
                        buyAgainFoodName.add(it)
                    }
                    listOfOrederList[i].foodPrice?.firstOrNull()?.let {
                        buyAgainFoodPrice.add(it)
                    }
                    listOfOrederList[i].foodImages?.firstOrNull()?.let {
                        buyAgainFoodImage.add(it)
                    }
                }
                val rv  =binding.buyAgainRecylerView
                rv.layoutManager=LinearLayoutManager(requireContext())
                buyAgainAdapter =BuyAgainAdapter(buyAgainFoodName,buyAgainFoodPrice,buyAgainFoodImage,requireContext())
                rv.adapter = buyAgainAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    private fun setUpRecyclerView(){



    }

    companion object {

    }
}