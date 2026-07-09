package com.example.practice

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class RestaurantAdapter(
    private val onItemClick: (Restaurant) -> Unit,
    private val onDeleteClick: (Restaurant) -> Unit
) : ListAdapter<Restaurant, RestaurantAdapter.RestaurantViewHolder>(RestaurantDiffCallback()) {

    class RestaurantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tvRestaurantName)
        val ratingTextView: TextView = itemView.findViewById(R.id.tvRestaurantRating)
        val deleteButton: View = itemView.findViewById(R.id.btnDelete)
        val deliveryTimeTextView: TextView = itemView.findViewById(R.id.tvDeliveryTime)
        val addressTextView: TextView = itemView.findViewById(R.id.tvAddress)
        val categoryIcon: ImageView = itemView.findViewById(R.id.ivCategoryIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = getItem(position)
        holder.nameTextView.text = restaurant.name
        holder.ratingTextView.text = restaurant.rating.toString()
        holder.deliveryTimeTextView.text = restaurant.deliveryTime
        holder.addressTextView.text = restaurant.address
        
        val iconRes = when (restaurant.category) {
            RestaurantCategory.CAFE -> R.drawable.ic_cafe
            RestaurantCategory.FAST_FOOD -> R.drawable.ic_fastfood
            RestaurantCategory.RESTAURANT -> R.drawable.ic_restaurant
        }
        holder.categoryIcon.setImageResource(iconRes)

        val iconTint: Int
        val bgTint: Int

        when (restaurant.category) {
            RestaurantCategory.CAFE -> {
                iconTint = 0xFF795548.toInt() // Brown
                bgTint = 0xFFF5F5F5.toInt()   // Light Grey
            }
            RestaurantCategory.FAST_FOOD -> {
                iconTint = 0xFFFFD600.toInt() // Yellow
                bgTint = 0xFFFFF9C4.toInt()   // Light Yellow
            }
            RestaurantCategory.RESTAURANT -> {
                iconTint = 0xFF2196F3.toInt() // Blue
                bgTint = 0xFFE3F2FD.toInt()   // Light Blue
            }
        }

        holder.categoryIcon.setColorFilter(iconTint)
        holder.categoryIcon.backgroundTintList = android.content.res.ColorStateList.valueOf(bgTint)
        
        holder.itemView.setOnClickListener {
            onItemClick(restaurant)
        }
        
        holder.deleteButton.setOnClickListener {
            onDeleteClick(restaurant)
        }
    }
}

class RestaurantDiffCallback : DiffUtil.ItemCallback<Restaurant>() {
    override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant): Boolean {
        return oldItem == newItem
    }
}
