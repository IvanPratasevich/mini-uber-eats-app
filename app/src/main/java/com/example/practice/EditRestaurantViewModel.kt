package com.example.practice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EditRestaurantViewModel : ViewModel() {
    private val _restaurant = MutableLiveData<Restaurant?>()
    val restaurant: LiveData<Restaurant?> = _restaurant

    private var isNew = false

    fun loadRestaurant(restaurantId: Int) {
        if (restaurantId == -1) {
            isNew = true
            _restaurant.value = null
        } else if (_restaurant.value == null) {
            _restaurant.value = RestaurantRepository.getRestaurantById(restaurantId)
        }
    }

    fun saveRestaurant(
        newName: String,
        newCategory: RestaurantCategory,
        newRating: Double,
        newDeliveryTime: String,
        newAddress: String,
        newDescription: String
    ) {
        val imageResId = when (newCategory) {
            RestaurantCategory.FAST_FOOD -> R.drawable.fastfood
            RestaurantCategory.CAFE -> R.drawable.cafe
            RestaurantCategory.RESTAURANT -> R.drawable.restaurant
        }
        
        if (isNew) {
            RestaurantRepository.addRestaurant(
                newName, newCategory, newRating, newDeliveryTime, newAddress, newDescription, imageResId
            )
        } else {
            val currentRestaurant = _restaurant.value ?: return
            RestaurantRepository.updateRestaurant(
                currentRestaurant,
                newName,
                newCategory,
                newRating,
                newDeliveryTime,
                newAddress,
                newDescription,
                imageResId
            )
        }
    }
}
