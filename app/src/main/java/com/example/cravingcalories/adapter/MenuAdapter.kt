package com.example.cravingcalories.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cravingcalories.DetailsActivity
import com.example.cravingcalories.databinding.MenuItemBinding

class MenuAdapter(private val menuItems: List<com.example.cravingcalories.Model.MenuItem>, private val requiredContext: Context) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val  binding =MenuItemBinding.inflate(LayoutInflater.from( parent.context),parent,false)
        return MenuViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
       holder.bind(position)
    }
    override fun getItemCount(): Int = menuItems.size
    inner class MenuViewHolder(private val binding: MenuItemBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener{
                val position =adapterPosition
                if (position != RecyclerView.NO_POSITION){
                    openDetailActivity(position)
                }

            }
        }

        private fun openDetailActivity(position: Int) {
            val menuItem =menuItems[position]

            val intent = Intent(requiredContext,DetailsActivity::class.java).apply {
                putExtra("MenuItemName",menuItem.foodName)
                putExtra("MenuItemPrice",menuItem.foodPrice)
                putExtra("MenuItemDescription",menuItem.foodDescription)
                putExtra("MenuItemImage",menuItem.foodImage)
                putExtra("MenuItemIngredients",menuItem.foodIngredient)

            }
            requiredContext.startActivity(intent)
        }

        fun bind(position: Int) {
            val menuItem= menuItems[position]
           binding.apply {
               menufoodName.text =  menuItem.foodName
               menuPrice.text =menuItem.foodPrice
               val Uri = Uri.parse(menuItem.foodImage)
               Glide.with(requiredContext).load(Uri).into(menuImage)



           }
        }

    }
    interface OnClickListener {
        fun onItemClick(position: Int)

    }
}


