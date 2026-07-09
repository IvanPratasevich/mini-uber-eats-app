package com.example.practice

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class RestaurantCategory(val displayName: String) {
    FAST_FOOD("Fast Food"),
    CAFE("Cafe"),
    RESTAURANT("Restaurant");

    override fun toString(): String = displayName
}

@Entity(tableName = "restaurants")
data class Restaurant(
    @PrimaryKey val id: Int,
    val name: String,
    val category: RestaurantCategory,
    val rating: Double,
    val deliveryTime: String,
    val address: String,
    val description: String,
    val imageResId: Int = R.drawable.restaurant
)
