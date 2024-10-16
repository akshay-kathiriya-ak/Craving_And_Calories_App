package com.example.cravingcalories.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.cravingcalories.MenuBottomSheetFragment
import com.example.cravingcalories.Model.MenuItem
import com.example.cravingcalories.R
import com.example.cravingcalories.adapter.MenuAdapter
import com.example.cravingcalories.databinding.FragmentHomeBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var database:FirebaseDatabase
    private lateinit var menuItems:MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.viewMenu.setOnClickListener {
            val bottomSheetDialog =MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"Test")
        }
        retriveAndDisplayPopularItem()

        return binding.root
    }

    private fun retriveAndDisplayPopularItem() {
        database =FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference = database.reference.child("Menu")
        menuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children){
                    val menuItem =foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
                randomPopularItem()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load data. Please try again.", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun randomPopularItem() {
        val index =menuItems.indices.toList().shuffled()
        val numItemToShow =6
        val subsetMenuItem =index.take(numItemToShow).map { menuItems[it] }
        setPopularItemAdapter(subsetMenuItem)
    }

    private fun setPopularItemAdapter(subsetMenuItem: List<MenuItem>) {
        val adapter =MenuAdapter(subsetMenuItem,requireContext())
        binding.PopularRecylerView.layoutManager =LinearLayoutManager(requireContext())
        binding.PopularRecylerView.adapter =adapter

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the image slider
        val imageList = ArrayList<SlideModel>() // Create image list
        imageList.add(SlideModel(R.drawable.banner1, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2, ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3, ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList, ScaleTypes.FIT)
        imageSlider.setItemClickListener(object :ItemClickListener{
            override fun doubleClick(position: Int) {
               Toast.makeText(context,"Select Your Favorite Food",Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(),itemMessage,Toast.LENGTH_SHORT).show()
            }
        })


    }
    companion object {

    }
}
