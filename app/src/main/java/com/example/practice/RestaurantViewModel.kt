package com.example.practice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RestaurantViewModel : ViewModel() {

    private val _uiState = MutableLiveData(RestaurantListUiState())
    val uiState: LiveData<RestaurantListUiState> = _uiState

    init {
        reloadRestaurants()
    }

    fun reloadRestaurants() {
        updateState(RestaurantRepository.getRestaurants())
    }

    private fun updateState(restaurants: List<Restaurant>) {
        _uiState.value = RestaurantListUiState(
            restaurants = restaurants,
            restaurantCount = restaurants.size,
            isEmpty = restaurants.isEmpty()
        )
    }

    fun addRestaurant(name: String, category: RestaurantCategory = RestaurantCategory.RESTAURANT) {
        val imageResId = when (category) {
            RestaurantCategory.FAST_FOOD -> R.drawable.fastfood
            RestaurantCategory.CAFE -> R.drawable.cafe
            RestaurantCategory.RESTAURANT -> R.drawable.restaurant
        }
        val updatedList = RestaurantRepository.addRestaurant(name, category, imageResId = imageResId)
        updateState(updatedList)
    }

    fun removeRestaurant(restaurant: Restaurant) {
        val updatedList = RestaurantRepository.removeRestaurant(restaurant)
        updateState(updatedList)
    }
}
