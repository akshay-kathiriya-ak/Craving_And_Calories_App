package com.example.cravingcalories

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cravingcalories.Model.OrderDetails
import com.example.cravingcalories.adapter.recentBuyAdapter
import com.example.cravingcalories.databinding.ActivityRecentOrderItemsBinding

class  recentOrderItemsActivity : AppCompatActivity() {

    private val binding:ActivityRecentOrderItemsBinding by lazy {
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }
    private lateinit var allFoodName:ArrayList<String>
    private lateinit var allFoodImage:ArrayList<String>
    private lateinit var allFoodPrice:ArrayList<String>
    private lateinit var allFoodQuantities:ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val recentOrderItems =intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetails>
        recentOrderItems?.let {
            orderDetails->
            if (orderDetails.isNotEmpty()){
                val recentOrderItem =orderDetails[0]
                allFoodName =recentOrderItem.foodNames as ArrayList<String>
                allFoodImage =recentOrderItem.foodImages as ArrayList<String>
                allFoodPrice =recentOrderItem.foodPrice as ArrayList<String>
                allFoodQuantities =recentOrderItem.foodQuantities as ArrayList<Int>
            }
        }
        setAdapter()

    }

    private fun setAdapter() {
        val rv =binding.recentBuyRecyclerView
        rv.layoutManager= LinearLayoutManager(this)
        val adapter =recentBuyAdapter(this,allFoodName,allFoodImage,allFoodPrice,allFoodQuantities)
        rv.adapter = adapter
    }
}