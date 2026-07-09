package com.example.practice

data class RestaurantListUiState(
    val restaurants: List<Restaurant> = emptyList(),
    val restaurantCount: Int = 0,
    val isEmpty: Boolean = true
)
