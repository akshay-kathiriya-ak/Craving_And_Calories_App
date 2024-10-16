package com.example.cravingcalories.Fragment

import android.os.Bundle
import android.view.LayoutInflater

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cravingcalories.Model.MenuItem
import com.example.cravingcalories.adapter.MenuAdapter
import com.example.cravingcalories.databinding.FragmentSearchBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    private lateinit var database:FirebaseDatabase
    private val originalMenuItems = mutableListOf<MenuItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        // retrieve menu item from database
        retrieveMenuItem()

        // SearchView implementation
        setUpSearchView()
    


        return binding.root
    }


    private fun retrieveMenuItem() {
        database =FirebaseDatabase.getInstance()
        val foodReference:DatabaseReference = database.reference.child("Menu")
        foodReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItems= foodSnapshot.getValue(MenuItem::class.java)
                    menuItems?.let {
                        originalMenuItems.add(it)
                    }
                }
                showAllMenu()
            }




            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }
    private fun showAllMenu() {
val filteredMenuItem =ArrayList(originalMenuItems)
        setAdapter(filteredMenuItem)
    }

    private fun setAdapter(filteredMenuItem: List<MenuItem>) {
      adapter =MenuAdapter(filteredMenuItem,requireContext())
        binding.menuRecyclerView.layoutManager =LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter = adapter
    }


    private fun setUpSearchView() {
        binding.searchView1.setOnQueryTextListener(object : SearchView.OnQueryTextListener,android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }
        })
    }

    private fun filterMenuItems(query: String) {

        val filteredMenuItems =originalMenuItems.filter {
            it.foodName ?.contains(query,ignoreCase = true)==true
        }
        setAdapter(filteredMenuItems)
    }

companion object{

}

}
