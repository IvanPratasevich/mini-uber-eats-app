package com.example.practice

import android.content.Context

class RestaurantRepository {

    companion object {
        private var restaurantDao: RestaurantDao? = null

        fun init(context: Context) {
            if (restaurantDao == null) {
                restaurantDao = AppDatabase.getDatabase(context).restaurantDao()
            }
        }

        fun getRestaurants(): List<Restaurant> = restaurantDao?.getAll() ?: emptyList()

        fun getRestaurantById(id: Int): Restaurant? = restaurantDao?.getById(id)

        fun addRestaurant(
            name: String,
            category: RestaurantCategory = RestaurantCategory.RESTAURANT,
            rating: Double = 0.0,
            deliveryTime: String = "",
            address: String = "",
            description: String = "",
            imageResId: Int = R.drawable.restaurant
        ): List<Restaurant> {
            val id = Utils.generateId()
            val newRestaurant = Restaurant(id, name, category, rating, deliveryTime, address, description, imageResId)
            restaurantDao?.insert(newRestaurant)
            return getRestaurants()
        }

        fun removeRestaurant(restaurant: Restaurant): List<Restaurant> {
            restaurantDao?.delete(restaurant)
            return getRestaurants()
        }

        fun updateRestaurant(
            restaurant: Restaurant,
            newName: String,
            newCategory: RestaurantCategory,
            newRating: Double,
            newDeliveryTime: String,
            newAddress: String,
            newDescription: String,
            newImageResId: Int
        ): List<Restaurant> {
            val updatedRestaurant = restaurant.copy(
                name = newName,
                category = newCategory,
                rating = newRating,
                deliveryTime = newDeliveryTime,
                address = newAddress,
                description = newDescription,
                imageResId = newImageResId
            )
            restaurantDao?.update(updatedRestaurant)
            return getRestaurants()
        }

        fun clearRestaurants(): List<Restaurant> {
            restaurantDao?.deleteAll()
            return getRestaurants()
        }
    }
}
