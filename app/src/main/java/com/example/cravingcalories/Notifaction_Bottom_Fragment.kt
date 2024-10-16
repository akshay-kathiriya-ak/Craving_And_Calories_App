package com.example.cravingcalories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cravingcalories.adapter.NotifactionAdapter
import com.example.cravingcalories.databinding.FragmentNotifactionBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class Notifaction_Bottom_Fragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotifactionBottomBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotifactionBottomBinding.inflate(inflater, container, false)
        val notifications = listOf(
            "Your Order Has Been Canceled Successfully",
            "Order Has Been Taken By The Driver",
            "Congrats Your Order Placed"
        )
        val notificationImages = listOf(R.drawable.sademoji, R.drawable.truck, R.drawable.congrats)
        val adapter = NotifactionAdapter(ArrayList(notifications), ArrayList(notificationImages))
        binding.notifiacationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notifiacationRecyclerView.adapter = adapter
        return binding.root
    }
}
